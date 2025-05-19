package com.movieticket.movie_ticket_booking.service;

import com.movieticket.movie_ticket_booking.model.Deal;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class DealService {
    private final String DATA_FILE = "data/deals.txt";
    private final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public DealService() {
        try {
            Files.createDirectories(Paths.get("data"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveDeal(Deal deal) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                Paths.get(DATA_FILE),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND)) {
            writer.write(String.join("|",
                    deal.getId(),
                    deal.getTitle(),
                    deal.getCode(),
                    deal.getDescription(),
                    String.valueOf(deal.getDiscountPercentage()),
                    deal.getValidFrom().format(DATE_FORMATTER),
                    deal.getValidUntil().format(DATE_FORMATTER),
                    deal.getApplicableTheaters(),
                    String.valueOf(deal.isActive())
            ));
            writer.newLine();
        }
    }

    public List<Deal> getAllDeals() {
        List<Deal> deals = new ArrayList<>();
        Path path = Paths.get(DATA_FILE);

        if (!Files.exists(path)) return deals;

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", -1);

                Deal deal = new Deal();
                deal.setId(parts[0]);
                deal.setTitle(parts[1]);
                deal.setCode(parts[2]);
                deal.setDescription(parts[3]);
                deal.setDiscountPercentage(Double.parseDouble(parts[4]));
                deal.setValidFrom(LocalDate.parse(parts[5], DATE_FORMATTER));
                deal.setValidUntil(LocalDate.parse(parts[6], DATE_FORMATTER));
                deal.setApplicableTheaters(parts[7]);
                deal.setActive(Boolean.parseBoolean(parts[8]));

                deals.add(deal);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return deals;
    }

    public boolean deleteDeal(String dealId) throws IOException {
        List<Deal> deals = getAllDeals();
        boolean removed = deals.removeIf(deal -> deal.getId().equals(dealId));

        if (removed) {
            saveAllDeals(deals);
        }
        return removed;
    }

    private void saveAllDeals(List<Deal> deals) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(DATA_FILE))) {
            for (Deal deal : deals) {
                writer.write(String.join("|",
                        deal.getId(),
                        deal.getTitle(),
                        deal.getCode(),
                        deal.getDescription(),
                        String.valueOf(deal.getDiscountPercentage()),
                        deal.getValidFrom().format(DATE_FORMATTER),
                        deal.getValidUntil().format(DATE_FORMATTER),
                        deal.getApplicableTheaters(),
                        String.valueOf(deal.isActive())
                ));
                writer.newLine();
            }
        }
    }
}
