package org.yottabase.yottaquake.ui.web.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.yottabase.yottaquake.core.BoundingBox;
import org.yottabase.yottaquake.core.EventFilter;
import org.yottabase.yottaquake.core.LatLng;

public class ParamsUtils {

	public static BoundingBox extractBoundingBox(HttpServletRequest request) {
		// init bbox
		BoundingBox box = null;

		String paramTopLeftLat = cleanParam(request.getParameter("topLeftLat"));
		String paramTopLeftLng = cleanParam(request.getParameter("topLeftLng"));
		String paramBottomRightLat = cleanParam(request
				.getParameter("bottomRightLat"));
		String paramBottomRightLng = cleanParam(request
				.getParameter("bottomRightLng"));

		if (paramTopLeftLat != null && paramTopLeftLng != null
				& paramBottomRightLat != null & paramBottomRightLng != null) {

			LatLng topLeft = new LatLng(Double.parseDouble(paramTopLeftLat),
					Double.parseDouble(paramTopLeftLng));
			LatLng bottomRight = new LatLng(
					Double.parseDouble(paramBottomRightLat),
					Double.parseDouble(paramBottomRightLng));

			box = new BoundingBox(topLeft, bottomRight);
		}

		return box;

	}

	public static EventFilter extractEventFilter(HttpServletRequest request) {

		EventFilter eventFilter = new EventFilter();
		
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

		// date from
		String paramFrom = cleanParam(request.getParameter("fromDate"));
		if (paramFrom != null) {
			
			try {
				eventFilter.setFrom(dateFormat.parse(paramFrom));
			} catch (ParseException e) {
				System.err.println("Data in formato non valido " + paramFrom);
			}
		}

		// date to
		String paramTo = cleanParam(request.getParameter("toDate"));
		if (paramTo != null) {
			try {
				eventFilter.setTo(dateFormat.parse(paramTo));
			} catch (ParseException e) {
				System.err.println("Data in formato non valido " + paramTo);
			}
		}

		// magnitude min
		String paramMinMagnitude = cleanParam(request.getParameter("minMagnitude"));
		if (paramMinMagnitude != null) {
			eventFilter.setMinMagnitude(Integer.valueOf(paramMinMagnitude));
		}

		// magnitude max
		String paramMaxMagnitude = cleanParam(request.getParameter("maxMagnitude"));
		if (paramMaxMagnitude != null) {
			eventFilter.setMaxMagnitude(Integer.valueOf(paramMaxMagnitude));
		}
		
		// magnitude type
		String paramMagnitudeType = cleanParam(request.getParameter("magnitudeType"));
		if (paramMagnitudeType != null) {
			eventFilter.setMagnitudeType(paramMagnitudeType);
		}

		// depth min
		String paramMinDepth = cleanParam(request.getParameter("minDepth"));
		if (paramMinDepth != null) {
			eventFilter.setMinDepth(Integer.valueOf(paramMinDepth));
		}

		// depth max
		String paramMaxDepth = cleanParam(request.getParameter("maxDepth"));
		if (paramMinDepth != null && paramMaxDepth != null) {
			eventFilter.setMaxDepth(Integer.valueOf(paramMaxDepth));
		}
		
		return eventFilter;

	}

	public static String cleanParam(String param) {

		if (param == null || param.length() == 0)
			return null;
		return param.trim();

	}

}
