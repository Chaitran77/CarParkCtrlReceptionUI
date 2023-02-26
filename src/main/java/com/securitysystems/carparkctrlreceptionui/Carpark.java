package com.securitysystems.carparkctrlreceptionui;

public class Carpark {
	// using the Integer class rather than int data type to permit possible null values received: https://stackoverflow.com/a/2254463/7169383
	public Integer CarparkID;
	public Integer TotalSpaces;
	public Integer FreeSpaces;

	public Carpark(Integer CarparkID, Integer TotalSpaces, Integer FreeSpaces) {
		this.CarparkID = CarparkID;
		this.TotalSpaces = TotalSpaces;
		this.FreeSpaces = FreeSpaces;
	}
}
