package io.github.whocaresonroad.reservation;

import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.shared.ui.combobox.FilteringMode;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;

import io.github.whocaresonroad.util.DateTimeUtil;

/*
 * UI class for selecting reservation slot.
 */
@SpringComponent
@UIScope
public class ReservationSlotSelector extends HorizontalLayout {

	/*
	 * Configuration class for getting settings.
	 */
	ReservationConfiguration reservationConfiguration;

	/*
	 * Form for getting the reserver information from the user.
	 */
	private final ReservationEditor reservationEditor;

	/*
	 * Spring repository.
	 */
	private final ReservationRepository reservationRepository;

	/*
	 * Grid for showing the reservation slots.
	 */
	final Grid reservationSlotsGrid;

	/*
	 * Combo box for user to select the length of the reservation.
	 */
	ComboBox reservationLenghtsComboBox = new ComboBox("Select duration");

	/*
	 * Currently selected reservation slot.
	 */
	ReservationSlot selectedSlot;

	@Autowired
	public ReservationSlotSelector(ReservationConfiguration reservationConfiguration, ReservationEditor resEditor, ReservationRepository reservationRepository) {
		this.reservationSlotsGrid = new Grid();
		this.reservationConfiguration = reservationConfiguration;
		this.reservationEditor = resEditor;
		this.reservationRepository = reservationRepository;

		// Build layout
		VerticalLayout slotLayout = new VerticalLayout(reservationLenghtsComboBox, reservationSlotsGrid);
		addComponents(slotLayout, reservationEditor);

		// Configure layouts and components
		setMargin(true);
		setSpacing(true);

		slotLayout.setSpacing(true);

		// Get slot lengths from settings and set combo box from them
		int [] slotLengts = reservationConfiguration.getReservationSlotLenghtsInMinutes();
		for (int item : slotLengts) {
			reservationLenghtsComboBox.addItem(item);
		}
		// Select the first
		reservationLenghtsComboBox.select(slotLengts[0]);
		reservationLenghtsComboBox.setFilteringMode(FilteringMode.CONTAINS);
		reservationLenghtsComboBox.setInvalidAllowed(false);
		reservationLenghtsComboBox.setNullSelectionAllowed(false);

		// Create slot for partially fake data
		selectedSlot = new ReservationSlot(new Date(),(int)reservationLenghtsComboBox.getValue(),reservationConfiguration.getDateFormat());

		// Wire action for the combo box
		reservationLenghtsComboBox.addValueChangeListener(event -> {
			// Remember the selection and update free slots
			selectedSlot.setSlotLenght((int)reservationLenghtsComboBox.getValue());
			fillReservationSlots();
			// Hide the editor because the slot selection is not valid anymore
			reservationEditor.clearReservation();
		});

		// Wire the column content to slot
		reservationSlotsGrid.setColumns("displayString");
		// Hide the column header
		reservationSlotsGrid.setHeaderVisible(false);

		// Wire action to grid selection
		reservationSlotsGrid.addSelectionListener(e -> {
			if (!e.getSelected().isEmpty()) {
				// Show the reservation editor with the user selection
				reservationEditor.startReservation((ReservationSlot)reservationSlotsGrid.getSelectedRow());
				// Don't remember the selection
				reservationSlotsGrid.deselectAll();
			}
		});

		// Get reservation slots
		fillReservationSlots();
	}

	/*
	 * Get free reservation slots and show them.
	 */
	protected void fillReservationSlots() {
		//	Get slots and prepare the presentation
		List<ReservationSlot> slots = reservationRepository.getFreeReservationSlots(new Date(),selectedSlot.getSlotLenght());
		Iterator<ReservationSlot> iter = slots.iterator();
		if (iter.hasNext()) {
			Calendar calendar = Calendar.getInstance();

			// Get start time from the first slot and remember the day
			ReservationSlot slot = iter.next();
			calendar.setTime(slot.getStartTime());
			int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

			// Show date for first slot
			slot.setDisplayString(DateTimeUtil.format(slot.getStartTime(),reservationConfiguration.getDateFormat()));

			// Assume the first slot is selected
			selectedSlot = slot;

			// Iterate all slots
			while (iter.hasNext()) {
				slot = iter.next();
				calendar.setTime(slot.getStartTime());

				//	When day changes, show the date, otherwise show only time
				if (currentDay != calendar.get(Calendar.DAY_OF_MONTH)) {
					slot.setDisplayString(DateTimeUtil.format(slot.getStartTime(),reservationConfiguration.getDateFormat()));
					currentDay = calendar.get(Calendar.DAY_OF_MONTH);
				} else {
					slot.setDisplayString(DateTimeUtil.format(slot.getStartTime(),reservationConfiguration.getTimeFormat()));
				}
			}
		}

		// Set as grid container data source
		reservationSlotsGrid.setContainerDataSource(new BeanItemContainer<ReservationSlot>(ReservationSlot.class,slots));
	}
}