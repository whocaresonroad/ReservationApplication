package io.github.whocaresonroad.reservation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * Error handling, just redirect to root url
 */
@Controller
public class ReservationErrorController implements ErrorController {

	private static final String PATH = "/error";

	@RequestMapping(value = "/error")
	String error(HttpServletRequest request, HttpServletResponse response) {
		return "redirect:/";
	}

	public String getErrorPath() {
		return PATH;
	}
}