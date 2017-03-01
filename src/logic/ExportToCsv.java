package logic;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import domain.FlexDrive;
import util.CleanUp;

public class ExportToCsv {

	public void exportToCsv(int year, int month, String path) {
		FTController ftController = new FTControllerImpl();
		List<FlexDrive> flexDriveList = new ArrayList<>();
		flexDriveList = ftController.fetchFlexDrivesCsvInfoByDate(year, month);
		FileWriter fileWriter = null;

		DecimalFormat decFormat = new DecimalFormat();
		DecimalFormatSymbols decSymbols = decFormat.getDecimalFormatSymbols();
		char decSeparator = decSymbols.getDecimalSeparator();
		String listSeperator = "";

		if (decSeparator == ',') {
			listSeperator = ";";
		} else if (decSeparator == '.') {
			listSeperator = ",";
		}

		try {
			fileWriter = new FileWriter(path);
			int i = 0;
			FlexDrive flexDrive = null;

			String titles = "";
			titles += "Navn" + listSeperator;
			titles += "Adresse" + listSeperator;
			titles += "Adresse" + listSeperator;
			titles += "Kommune fra" + listSeperator;
			titles += "Kommune til" + listSeperator;
			titles += "Pris" + listSeperator;
			titles += "Bestilt til" + listSeperator;
			titles += "Bestilt fra" + listSeperator;
			titles += "passagerer" + listSeperator;
			titles += "bagage" + listSeperator;
			titles += "barnevogn" + listSeperator;
			titles += "Børnebilsæder" + listSeperator;
			titles += "Hjælpemidler" + listSeperator;
			titles += "Kommentar" + listSeperator;
			titles += "Biler" + listSeperator;

			fileWriter.append(titles + "\r\n");

			for (i = 0; i < flexDriveList.size(); i++) {
				flexDrive = flexDriveList.get(i);
				String flexDrivesString = "";

				flexDrivesString += flexDrive.getUsername() + listSeperator;
				flexDrivesString += flexDrive.getAddressFrom() + listSeperator;
				flexDrivesString += flexDrive.getAddressTo() + listSeperator;
				flexDrivesString += flexDrive.getFrom() + listSeperator;
				flexDrivesString += flexDrive.getTo() + listSeperator;
				flexDrivesString += flexDrive.getPrice().getValue() + listSeperator;
				flexDrivesString += flexDrive.getBookedFor().toString() + listSeperator;
				flexDrivesString += flexDrive.getBookedAt().toString() + listSeperator;
				flexDrivesString += flexDrive.getPassengers() + listSeperator;
				flexDrivesString += flexDrive.getLuggage() + listSeperator;
				flexDrivesString += flexDrive.getPram() + listSeperator;
				flexDrivesString += flexDrive.getChildCarSeat() + listSeperator;
				flexDrivesString += flexDrive.getAssistive() + listSeperator;
				flexDrivesString += flexDrive.getComment() + listSeperator;
				flexDrivesString += flexDrive.getCar().getRegistryPlate() + listSeperator;

				fileWriter.append(flexDrivesString + "\r\n");
			}
			fileWriter.flush();
		} catch (IOException exc) {

		} finally {
			CleanUp.cleanUpWriter(fileWriter);
		}
	}

}
