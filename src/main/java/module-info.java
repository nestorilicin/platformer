module rs.etf.nestor.platformer {
    requires javafx.controls;
    requires javafx.fxml;


    opens rs.etf.nestor.platformer to javafx.fxml;
    exports rs.etf.nestor.platformer.main;
}