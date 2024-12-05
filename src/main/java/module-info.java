module assignment.librarymanagement {
	requires com.google.zxing;
	requires com.google.zxing.javase;
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.jetbrains.annotations;
	requires java.management;

	exports assignment.librarymanagement;
	exports assignment.librarymanagement.data;
	exports assignment.librarymanagement.controllers;
	exports assignment.librarymanagement.functions;
	exports assignment.librarymanagement.managers;
	exports assignment.librarymanagement.utils;
	exports assignment.librarymanagement.controllers.interfaces;
}