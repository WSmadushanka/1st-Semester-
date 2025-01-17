package lk.ijse.gdse71.nl.nltecnologies.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lk.ijse.gdse71.nl.nltecnologies.MyListener;
import lk.ijse.gdse71.nl.nltecnologies.dto.ItemCard;

public class ItemCardController {

    @FXML
    private AnchorPane card_form;

    @FXML
    private ImageView itemImage;

    @FXML
    private Label lblItemName;

    @FXML
    private Label lblPrice;

    private ItemCard itemCard;
    private MyListener myListener;

    public void setData(ItemCard itemCard, MyListener myListener){
        this.itemCard = itemCard;
        this.myListener = myListener;

        lblItemName.setText(itemCard.getItemName());
        double profit = itemCard.getPrice() * 0.25;
        double netPrice = itemCard.getPrice() + profit;

        lblPrice.setText(String.valueOf(netPrice));
        Image image = new Image(itemCard.getImage(),190,155,false,true);
        itemImage.setImage(image);
    }

    public void click(javafx.scene.input.MouseEvent mouseEvent) {
        myListener.onClickListener(itemCard);
    }
}
