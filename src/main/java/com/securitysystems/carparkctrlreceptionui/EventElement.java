package com.securitysystems.carparkctrlreceptionui;

/*
what EventElement would be if AnchorPane could be subclassed

public class EventElement extends AnchorPane {
	public final Log log;
	public final AnchorPane eventElement;

	public EventElement(Log logObject) throws IOException {
		super();

		this.log = logObject;
		this.eventElement = FXMLLoader.load(getClass().getResource("/carpark-monitoring-view/EventElement.fxml"));

		((Label) this.eventElement.lookup("#numberplate-label")).setText((this.log.Numberplate!=null)?(this.log.Numberplate):("Unknown"));
		((Label) this.eventElement.lookup("#entry-timestamp-label")).setText((this.log.EntryTimestamp!=null)?(this.log.EntryTimestamp.toString()):("Unknown"));
		((Label) this.eventElement.lookup("#exit-timestamp-label")).setText((this.log.ExitTimestamp!=null)?(this.log.ExitTimestamp.toString()):("Unknown"));
	}
}
*/
