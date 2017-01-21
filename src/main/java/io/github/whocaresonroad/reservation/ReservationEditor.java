package io.github.whocaresonroad.reservation;

import org.springframework.beans.factory.annotation.Autowired;

/*
import com.vaadin.data.validator.StringLengthValidator;
import com.vaadin.server.FontAwesome;
*/

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import io.github.whocaresonroad.util.DateTimeUtil;

/*
 * UI class for getting the reserver information from the user.
 */
@SpringComponent
@UIScope
public class ReservationEditor extends VerticalLayout {

	/*
	 * Spring repository.
	 */
	private final ReservationRepository reservationRepository;

	/*
	 * Configuration class for getting settings.
	 */
	@Autowired
	ReservationConfiguration reservationConfiguration;

	/*
	 * Reservation to hold the values.
	 */
	private Reservation reservation = new Reservation();

	// Label to show the reservation start time.
	Label startTime = new Label("StartTime");

	// Text fields
	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	TextField address = new TextField("Address");
	TextField postalCode = new TextField("Postal code");
	TextField city = new TextField("City");

	// Action buttons
	Button reserve = new Button("Reserve");
	Button cancel = new Button("Cancel");

	// Layout for buttons
	CssLayout actions = new CssLayout(reserve, cancel);

	/*
	 * Create the UI
	 */
	@Autowired
	public ReservationEditor(ReservationRepository reservationRepository) {

		this.reservationRepository = reservationRepository;

		//	Add all components
		addComponents(startTime, firstName, lastName, address, postalCode, city, actions);

		// Configure and style components
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		reserve.setStyleName(ValoTheme.BUTTON_PRIMARY);
		reserve.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		setMargin(true);
		setSpacing(true);

		// Wire action to buttons
		reserve.addClickListener(e -> createReservation());
		cancel.addClickListener(e -> clearReservation());

		// Keep hidden until the user has selected start time
		setVisible(false);

		// Bind text field values to the reservation
		BeanFieldGroup.bindFieldsUnbuffered(reservation, this);
	}

	/*
	 * Add clicked, create reservation to db
	 */
	public void createReservation() {
		//	Save the reservation
		if (reservationRepository.saveAndCheck(reservation) == null) {
			// Could not save the reservation because the time slot was already taken, notify the user
			Notification.show("Could not make the reservation",
	                  "It looks like someone already made a reservation for the same time. Please try again.",
	                  Notification.Type.ERROR_MESSAGE);
			} else {
				String reservationDetails = "Reservation made. Details:\n ";
				reservationDetails += DateTimeUtil.format(reservation.getStartTime(),reservationConfiguration.getDateFormat());
				reservationDetails += " - " + DateTimeUtil.format(reservation.getEndTime(),reservationConfiguration.getTimeFormat());
				reservationDetails += "\n" + reservation.getFirstName() + " " + reservation.getLastName();
				reservationDetails += "\n" + reservation.getAddress();
				reservationDetails += "\n" + reservation.getPostalCode() + " " + reservation.getCity();
				
				Notification.show(reservationDetails,
		                  Notification.Type.WARNING_MESSAGE);
			}

		//	Hide editor and ask UI to Update the reservation slots
		clearReservation();
		((ReservationSlotSelector)getParent()).fillReservationSlots();
	}

	/*
	 * User has selected reservation slot, show editor.
	 */
	public void startReservation(ReservationSlot reservationSlot) {
		//	Set start and end times
		reservation.setStartTime(reservationSlot.getStartTime());
		reservation.setEndTime(reservationSlot.getEndTime());

		// Show the selected time in the label
		startTime.setValue(DateTimeUtil.format(reservationSlot.getStartTime(),reservationConfiguration.getDateFormat()));

		// Request focus and show the editor
		firstName.focus();
		setVisible(true);
	}

	/*
	 * Remember the values and hide the editor.
	 */
	public void clearReservation() {
		setVisible(false);
		// Copy values and bind text field values to the reservation
		reservation = new Reservation(reservation);
		BeanFieldGroup.bindFieldsUnbuffered(reservation, this);
	}
}
