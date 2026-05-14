package com.skills4it.dealership.data;

import com.skills4it.dealership.models.Dealership;
import com.skills4it.dealership.models.Vehicle;
import com.skills4it.dealership.models.enums.VehicleType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Handles loading and saving dealership inventory data from and to a CSV file.
 *
 * This class separates file handling from the business logic in the Dealership class.
 * That keeps the application cleaner and easier to maintain.
 */
public class DealershipFileManager {

	// Path to the inventory CSV file inside the Maven resources folder.
	private static final Path INVENTORY_PATH = Path.of("src", "main", "resources", "inventory.csv");

	// Regex delimiter for splitting pipe-separated CSV lines.
	// The pipe symbol has special meaning in regex, so it must be escaped.
	private static final String DELIMITER = "\\|";

	/**
	 * Loads the dealership and all vehicles from the inventory file.
	 *
	 * The first line contains dealership information:
	 * name|address|phone
	 *
	 * Every next line contains one vehicle:
	 * vin|year|make|model|type|color|odometer|price
	 */
	public Dealership getDealership() {
		// Make sure the expected file exists before trying to read it.
		ensureInventoryFileExists();

		// try-with-resources automatically closes the BufferedReader after reading.
		try (BufferedReader reader = Files.newBufferedReader(INVENTORY_PATH)) {

			// The first line contains the dealership data.
			String dealershipLine = reader.readLine();

			if (dealershipLine == null || dealershipLine.isBlank()) {
				throw new IllegalStateException("The inventory file is empty.");
			}

			// Split the dealership line into name, address, and phone.
			String[] dealershipFields = dealershipLine.split(DELIMITER);
			if (dealershipFields.length != 3) {
				throw new IllegalStateException("Invalid dealership line. Expected: name|address|phone");
			}

			// Create the Dealership object using the first CSV line.
			Dealership dealership = new Dealership(dealershipFields[0], dealershipFields[1], dealershipFields[2]);

			String vehicleLine;

			// Read the remaining lines. Each line should represent one vehicle.
			while ((vehicleLine = reader.readLine()) != null) {

				// Skip empty lines so blank rows in the file do not crash the program.
				if (vehicleLine.isBlank()) {
					continue;
				}

				// Convert the CSV line into a Vehicle object and add it to the dealership inventory.
				dealership.addVehicle(parseVehicle(vehicleLine));
			}

			return dealership;
		} catch (IOException e) {

			// Wrap the checked IOException in an unchecked exception with a clear message.
			throw new IllegalStateException("Could not read inventory file: " + INVENTORY_PATH, e);
		}
	}

	/**
	 * Saves the current dealership data back to the inventory file.
	 *
	 * Before overwriting the original file, a backup copy is created.
	 */
	public void saveDealership(Dealership dealership) {
		// Check that the file exists before saving.
		ensureInventoryFileExists();

		// Create a timestamped backup before overwriting the inventory file.
		createBackupFile();

		// try-with-resources automatically closes the BufferedWriter after writing.
		try (BufferedWriter writer = Files.newBufferedWriter(INVENTORY_PATH)) {

			// Write the dealership data as the first line.
			writer.write(dealership.toCsvHeaderLine());
			writer.newLine();

			// Write every vehicle as one CSV line.
			List<Vehicle> vehicles = dealership.getAllVehicles();
			for (Vehicle vehicle : vehicles) {
				writer.write(vehicle.toCsvLine());
				writer.newLine();
			}
		} catch (IOException e) {

			// Give a clear error if writing to the file fails.
			throw new IllegalStateException("Could not save inventory file: " + INVENTORY_PATH, e);
		}
	}

	/**
	 * Converts one CSV vehicle line into a Vehicle object.
	 *
	 * Expected format:
	 * vin|year|make|model|type|color|odometer|price
	 */
	private Vehicle parseVehicle(String vehicleLine) {
		// Split the line into separate fields using the pipe delimiter.
		String[] fields = vehicleLine.split(DELIMITER);

		// A vehicle line must have exactly 8 fields.
		if (fields.length != 8) {
			throw new IllegalStateException("Invalid vehicle line. Expected 8 fields: " + vehicleLine);
		}

		try {
			// Convert the text fields from the CSV file into the correct Java data types.
			int vin = Integer.parseInt(fields[0]);
			int year = Integer.parseInt(fields[1]);
			String make = fields[2];
			String model = fields[3];

			// Convert the text value to a VehicleType enum.
			// If the text does not match a valid type, throw a clear error.
			VehicleType vehicleType = VehicleType.fromString(fields[4])
					.orElseThrow(() -> new IllegalStateException("Invalid vehicle type in vehicle line: " + vehicleLine));

			String color = fields[5];
			int odometer = Integer.parseInt(fields[6]);
			double price = Double.parseDouble(fields[7]);

			// Create and return a Vehicle object with the parsed data.
			return new Vehicle(vin, year, make, model, vehicleType, color, odometer, price);
		} catch (NumberFormatException e) {

			// This catches invalid numbers, such as "abc" for year, odometer, or price.
			throw new IllegalStateException("Invalid number in vehicle line: " + vehicleLine, e);
		}
	}

	/**
	 * Verifies that the inventory file exists.
	 *
	 * This method fails fast with a clear error message if the file is missing.
	 */
	private void ensureInventoryFileExists() {
		if (!Files.exists(INVENTORY_PATH)) {
			throw new IllegalStateException("Inventory file not found at: " + INVENTORY_PATH.toAbsolutePath());
		}
	}

	/**
	 * Creates a timestamped backup copy of the inventory file.
	 *
	 * Example backup filename:
	 * inventory-20260514-211530.csv
	 */
	private void createBackupFile() {
		try {
			// Create a backups folder next to the inventory file.
			Path backupDirectory = INVENTORY_PATH.getParent().resolve("backups");
			Files.createDirectories(backupDirectory);

			// Use a timestamp so every backup gets a unique filename.
			String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
			Path backupPath = backupDirectory.resolve("inventory-" + timestamp + ".csv");

			// Copy the current inventory file to the backup folder.
			Files.copy(INVENTORY_PATH, backupPath, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {

			// Stop saving if the backup could not be created.
			throw new IllegalStateException("Could not create inventory backup file.", e);
		}
	}
}