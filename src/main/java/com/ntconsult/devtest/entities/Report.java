package com.ntconsult.devtest.entities;

import exceptions.BadFormatException;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Marcos Freitas Nunes <marcos@cognitivabrasil.com.br>
 */
public class Report {

    private final Logger log = LoggerFactory.getLogger(Report.class);

    private final Set<String> clients;
    private Sale mostExpensiveSale;

    /**
     * It is assumed that don't exists two sellers with the same name because the sale is related to the name of the
     * seller.
     */
    private final Map<String, Float> sellesWithTotalSale;

    public Report() {
        clients = new HashSet<>();
        sellesWithTotalSale = new HashMap<>();
        //initialize here to avoid null test in all lines
        mostExpensiveSale = new Sale();
    }

    /**
     * Adds a file to this report and calculate the partial datas.
     *
     * @param file
     */
    public void addFile(File file) {
        log.debug("Reading file: {}", file.getName());
        try {
            try (Scanner scanner = new Scanner(file)) {
                while (scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    //split line to identify the entity type
                    String[] lineArray = line.split("ç");
                    try {

                        //Tests if the array has the right number of columns.
                        if (lineArray.length != 4) {
                            throw new BadFormatException("The entity Seller must to have 4 attributes.");
                        }

                        Integer id = Integer.parseInt(lineArray[0]);
                        switch (id) {

                            case 1:
                                //seller
                                String sellerName = lineArray[2];
                                if (!sellesWithTotalSale.containsKey(sellerName)) {
                                    sellesWithTotalSale.put(sellerName, 0f);
                                }
                                break;
                            case 2:
                                //client
                                clients.add(lineArray[1]);
                                break;
                            case 3:
                                //sales data
                                try {
                                    Sale s = Sale.fromArray(lineArray);
                                    addSellersTotalSale(s);
                                    updateMostExpensiveSale(s);
                                } catch (BadFormatException formatEx) {
                                    log.error("This line will be ignored because is out of pattern. Line: {}",
                                            line, formatEx);
                                }
                                break;
                            default:
                                log.error("An unknown id was received. The possible values are 001, 002 or 003. Line: {}", line);
                                break;
                        }
                    } catch (NumberFormatException | BadFormatException nfEx) {
                        log.error("This line will be ignored because is out of pattern: {}", line, nfEx);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            log.error("File not found.", e);
        }
    }

    /**
     * Adds or updates the total sales of the seller.
     *
     * @param s
     */
    private void addSellersTotalSale(Sale s) {
        if (sellesWithTotalSale.containsKey(s.getSeller())) {
            Float sellerAmount = sellesWithTotalSale.get(s.getSeller());
            sellerAmount += s.getAmount();
            sellesWithTotalSale.put(s.getSeller(), sellerAmount);
        } else {
            sellesWithTotalSale.put(s.getSeller(), s.getAmount());
        }
    }

    /**
     * Will return the first one more expensive.
     *
     * @param s
     */
    private void updateMostExpensiveSale(Sale s) {
        if (s.getAmount() > mostExpensiveSale.getAmount()) {
            mostExpensiveSale = s;
        }
    }

    /**
     * Gets the number of clients listed in the files.
     *
     * @return number of clients.
     */
    public int getNumberOfClients() {
        return clients.size();
    }

    /**
     * Gets the number of sellers listed in the files.
     *
     * @return number of sellers in the files.
     */
    public int getNumberOfSellers() {
        return sellesWithTotalSale.size();
    }

    /**
     * Gets the most expensive sale.
     *
     * @return
     */
    public Sale getMostExpensiveSale() {
        return mostExpensiveSale;
    }

    /**
     * Gets the worst saller. The worst seller is the one which has the lowest sales total value.
     * <strong>If there are more then one seller with the same amount it will be returned only one, because the problem
     * specificied.</strong>
     *
     * @return Worst seller name
     */
    public String getWorstSellerName() {
        Entry<String, Float> min = Collections.min(sellesWithTotalSale.entrySet(),
                Comparator.comparing(Entry::getValue));
        return min.getKey();
    }

}
