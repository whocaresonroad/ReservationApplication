package io.github.whocaresonroad.reservation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.data.util.converter.Converter.ConversionException;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/*
 * UI class for managing reservations and settings.
 */
@SpringComponent
@UIScope
public class ReservationManager extends VerticalLayout {

	/*
	 * Configuration class for getting settings.
	 */
	ReservationConfiguration reservationConfiguration;

	/*
	 * Spring repository.
	 */
	private final ReservationRepository reservationRepository;

	/*
	 * Button for refreshing the reservations list.
	 */
	private final Button refreshButton = new Button("Refresh");

	/*
	 * Button for removing the reservation.
	 */
	private final Button removeButton = new Button("Remove");

	/*
	 * Grid for showing the reservation lengths.
	 */
	final Grid reservationLenghtsGrid = new Grid("Durations of the reservations");

	/*
	 * Text field for reservation lengths.
	 */
	private final TextField lengthField = new TextField("Duration");

	/*
	 * Button for removing reservation lengths.
	 */
	private final Button removeLengthButton = new Button("Remove duration");

	/*
	 * Button for adding reservation lengths.
	 */
	private final Button addLengthButton = new Button("Add duration");

	/*
	 * Text field for reservation slots.
	 */
	private final TextField slotsField = new TextField("Reservation slots");

	/*
	 * Button for adding reservation slots.
	 */
	private final Button saveSlotsButton = new Button("Save reservation slots");

	/*
	 * Grid for showing the reservations.
	 */
	final Grid reservationGrid = new Grid("Reservations");

	/*
	 * Currently selected reservation slot.
	 */
	ReservationSlot selectedSlot;

    /*
	 * Create the UI
	 */
	@Autowired
	public ReservationManager(ReservationRepository reservationRepository, ReservationConfiguration reservationConfiguration) {

		this.reservationRepository = reservationRepository;
		this.reservationConfiguration = reservationConfiguration;

		VerticalLayout lengthControlLayout = new VerticalLayout(lengthField,addLengthButton,removeLengthButton);
		VerticalLayout reservationSlotControlLayout = new VerticalLayout(slotsField,saveSlotsButton);
		HorizontalLayout topLayout = new HorizontalLayout(reservationLenghtsGrid,lengthControlLayout,reservationSlotControlLayout);
		HorizontalLayout reservationButtonLayout = new HorizontalLayout(refreshButton,removeButton);

		addComponents(topLayout,reservationButtonLayout,reservationGrid);

		setMargin(true);
		setSpacing(true);

		fillReservationLengths();

		// Configure layouts and components
		lengthControlLayout.setSpacing(true);
		reservationSlotControlLayout.setSpacing(true);
		reservationButtonLayout.setSpacing(true);
		topLayout.setSpacing(true);

		reservationLenghtsGrid.setHeight(190, Unit.PIXELS);
		reservationLenghtsGrid.setWidth(400, Unit.PIXELS);
		reservationLenghtsGrid.setHeaderVisible(false);
		reservationGrid.setColumns("startTime", "endTime", "firstName", "lastName", "address", "postalCode", "city", "id", "createdDate");

		reservationGrid.setHeight(500, Unit.PIXELS);
		reservationGrid.setWidth(1600, Unit.PIXELS);
		reservationLenghtsGrid.setColumns("length");

		// Set return shortcut to length addition button
		addLengthButton.setStyleName(ValoTheme.BUTTON_PRIMARY);
		addLengthButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);

		// Set text fields to convert to numbers
		lengthField.setConverter(Integer.class);
		slotsField.setConverter(Integer.class);
		lengthField.setNullRepresentation("");
		slotsField.setNullRepresentation("");

		slotsField.setCaption("Reservation slots: " + reservationConfiguration.getNumberOfFreeSlotsToShow());

		// Wire the handlers
		refreshButton.addClickListener(e -> fillReservations());

		removeButton.addClickListener(e -> {
			ReservationRepresentation selected = (ReservationRepresentation)reservationGrid.getSelectedRow();
			if (selected != null) {
				reservationRepository.delete(selected.getId());
				fillReservations();
			}
		});

		addLengthButton.addClickListener(e -> {
			// Add the new length to the settings and update grid
			String value = lengthField.getValue();
			if (value != "") {
				try {
					int convertedValue = (Integer) lengthField.getConvertedValue();
					if (!reservationConfiguration.addReservationSlotLenghtsInMinutes(convertedValue)) {
						Notification.show("Already exist: " + value, Notification.Type.WARNING_MESSAGE);
					}
				    fillReservationLengths();
				} catch (ConversionException ex) {
					Notification.show("Not a number: " + value, Notification.Type.WARNING_MESSAGE);
					}
			}
		});

		removeLengthButton.addClickListener(e -> {
			// Remove selected length from the settings and update grid
			ReservationLengthItem selected = (ReservationLengthItem)reservationLenghtsGrid.getSelectedRow();
			if (selected != null) {
				if (!reservationConfiguration.removeReservationSlotLenghtsInMinutes(Integer.parseInt(selected.getLength()))) {
					Notification.show("Can't remove the last duration", Notification.Type.WARNING_MESSAGE);
				}
		    	fillReservationLengths();
			}
		});

		saveSlotsButton.addClickListener(e -> {
			// Save the slots number
			String value = slotsField.getValue();
			if( value != "") {
				try {
					int convertedValue = (Integer) slotsField.getConvertedValue();
					reservationConfiguration.setNumberOfFreeSlotsToShow(convertedValue);
					slotsField.setCaption("Reservation slots: "+value);
					} catch (ConversionException ex) {
						Notification.show("Not a number: " + value, Notification.Type.WARNING_MESSAGE);
					}
			}
		});

		// Fill the reservation grid
		fillReservations();
	}

	private void fillReservations() {
		List<Reservation> reservations = reservationRepository.findAll();
		List<ReservationRepresentation> representations = new ArrayList<ReservationRepresentation>(reservations.size());
		String formatString = reservationConfiguration.getDateFormat();
	
		for (Reservation reservation : reservations) {
			representations.add(new ReservationRepresentation(reservation,formatString));
		}
	
		reservationGrid.setContainerDataSource(
				new BeanItemContainer<ReservationRepresentation>(ReservationRepresentation.class, representations));
	}

	private void fillReservationLengths()
	{
		int [] slotLengts = reservationConfiguration.getReservationSlotLenghtsInMinutes();
		List<ReservationLengthItem> slotLengthList = new ArrayList<ReservationLengthItem>(slotLengts.length);

		for (int item : slotLengts) {
			slotLengthList.add(new ReservationLengthItem(Integer.toString(item)));
		}

		reservationLenghtsGrid.setContainerDataSource(new BeanItemContainer<ReservationLengthItem>(ReservationLengthItem.class, slotLengthList));
	}
}