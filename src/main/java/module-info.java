module rs.etf.nestor.platformer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;


    opens rs.etf.nestor.platformer to javafx.fxml;
    exports rs.etf.nestor.platformer.main;
    exports rs.etf.nestor.platformer.cameras;
}