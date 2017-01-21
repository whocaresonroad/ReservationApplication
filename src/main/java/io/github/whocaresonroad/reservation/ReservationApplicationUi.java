package io.github.whocaresonroad.reservation;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.ui.UI;
import com.vaadin.annotations.Theme;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;

/*
 * Main UI component for the reservation application, created by the Vaadin framework.
 */
@SpringUI
@Theme("valo")
public class ReservationApplicationUi extends UI {

	/*
	 * Form for getting the reserver information from the user.
	 */
	private final ReservationSlotSelector reservationSlotSelector;

	/*
	 * Management form for reservations and settings.
	 */
	private ReservationManager reservationManager;

	/*
	 * Constructor, just remember the parameters.
	 */
	@Autowired
	public ReservationApplicationUi(ReservationSlotSelector reservationSlotSelector,
			ReservationManager reservationManager) {
		this.reservationSlotSelector = reservationSlotSelector;
		this.reservationManager = reservationManager;
	}

	/*
	 * Called by the Vaadin framework to build the UI.
	 * 
	 * @see com.vaadin.ui.UI#init(com.vaadin.server.VaadinRequest)
	 */
	@Override
	protected void init(VaadinRequest request) {
		String manager = request.getParameter("manager");
		boolean showManager = false;
		if (manager != null) {
			if (manager.equalsIgnoreCase("true")) {
				showManager = true;
			}
		}

		//	if the url contains manager=true value, use the manager view, otherwise use the reservation view
		if (showManager) {
			setContent(reservationManager);
			Page.getCurrent().setTitle("ReservationManager");
		} else {
			setContent(reservationSlotSelector);
			Page.getCurrent().setTitle("Reservation");
		}
		setSizeFull();
	}
}