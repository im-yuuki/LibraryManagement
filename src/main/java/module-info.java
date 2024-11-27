module assignment.librarymanager {
	requires com.google.zxing;
	requires com.google.zxing.javase;
	requires io.github.cdimascio.dotenv.java;
	requires java.sql;
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.graphics;
	requires org.jetbrains.annotations;

	exports assignment.librarymanager;
	exports assignment.librarymanager.data;
	exports assignment.librarymanager.functions;
	exports assignment.librarymanager.managers;
	exports assignment.librarymanager.utils;
}