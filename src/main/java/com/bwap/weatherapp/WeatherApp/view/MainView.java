package com.bwap.weatherapp.WeatherApp.view;

import com.bwap.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;


@SpringUI(path = "")
public class MainView extends UI {
    @Autowired
    private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private  NativeSelect<String> unitSelect;
    private  TextField cityTextFeild;
    private Button searchButton;
    private Label location ;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMin;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label feelsLike;
    private Image iconImg;
    private HorizontalLayout dashboard;
    private HorizontalLayout mainDescriptionLayout;
    private Image logo;
    private HorizontalLayout footer;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        mainLayout();
        setHeader();
        setLogo();
        setForm();
        dashboardTitle();
        dashboardDetails();
        searchButton.addClickListener(clickEvent -> {
            if (!cityTextFeild.getValue().equals("")){
                try {
                    updateUI();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
                Notification.show("Lutfen sehir giriniz !");
        });

    }

    private void updateUI() throws JSONException {
        String city = cityTextFeild.getValue();
        String defaultUnit;
        weatherService.setCityName(city);

        if(unitSelect.getValue().equals("F")){
            weatherService.setUnit("");
            unitSelect.setValue("F");
            defaultUnit="\u00b0"+"F";
            location.setValue("Suanda "+city+" hava durumu");
            JSONObject mainObject= weatherService.returnCurrentInfo();
            int temp = mainObject.getInt("temp_f");
            currentTemp.setValue(temp + defaultUnit);
        }else{
            weatherService.setUnit("");
            defaultUnit="\u00b0"+"C";
            unitSelect.setValue("C");
            location.setValue("Suanda "+city+" hava durumu");
            JSONObject mainObject= weatherService.returnCurrentInfo();
            int temp = mainObject.getInt("temp_c");
            currentTemp.setValue(temp + defaultUnit);
        }

        //yapamadım
        //weatherDescription.setValue("Acıklama: "+weatherService.returnCurrentInfo().getInt("condition"));
        //Updating Pressure
        pressureLabel.setValue("Basınc: "+weatherService.returnCurrentInfo().getInt("pressure_mb"));
        //Updating Humidity
        humidityLabel.setValue("Nem : "+weatherService.returnCurrentInfo().getInt("humidity"));

        //Updating Wind
        windSpeedLabel.setValue("Ruzgar : "+weatherService.returnCurrentInfo().getInt("wind_mph")+"m/s");
        //Updating Feels Like
        feelsLike.setValue("Hissedilen Sıcaklık : "+weatherService.returnCurrentInfo().getDouble("feelslike_c"));




    }
    private void setForm() {
        HorizontalLayout formLayout= new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);


        //C mi F mi karar vermek için
        unitSelect = new NativeSelect<>();
        ArrayList<String> items = new ArrayList<>();
        items.add("C");
        items.add("F");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(0));
        formLayout.addComponent(unitSelect);

        //şehir ismi texti
        cityTextFeild = new TextField();
        cityTextFeild.setWidth("80%");
        formLayout.addComponent(cityTextFeild);


        //arama buttonu
        searchButton=new Button();
        searchButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(searchButton);

        mainLayout.addComponents(formLayout);

    }
    private void setLogo() {
        HorizontalLayout logo = new HorizontalLayout();
        logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Image img = new Image(null,new ClassResource("/static/logoo.png"));
        logo.setWidth("248px");
        logo.setHeight("240px");

        logo.addComponent(img);
        mainLayout.addComponents(logo);
    }
    private void mainLayout() {
        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setSpacing(true);
        mainLayout.setMargin(true);
        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        setContent(mainLayout);
    }
    private void setHeader(){
        HorizontalLayout header = new HorizontalLayout();
        header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        Label title = new Label("Hava durumu");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);
        header.addComponent(title);

        mainLayout.addComponents(header);

    }
    private  void dashboardTitle(){
        dashboard= new HorizontalLayout();
        dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        //sehir lokasyonu
        location = new Label("Suanda  : ");
        location.addStyleName(ValoTheme.LABEL_H2);
        location.addStyleName(ValoTheme.LABEL_LIGHT);


        // Derece

        currentTemp= new Label(" ");
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_H1);

        dashboard.addComponents(location,currentTemp);
        mainLayout.addComponents(dashboard);


    }
    private void dashboardDetails(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        //acıklama
        VerticalLayout destiptionlayout = new VerticalLayout();
        destiptionlayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);


        //hava durumu acıklaması
        weatherDescription = new Label("Acıklama : ");
        weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
        destiptionlayout.addComponents(weatherDescription);



        VerticalLayout pressureLayout= new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLabel = new Label("Basınc : ");
        pressureLayout.addComponents(pressureLabel);

        humidityLabel = new Label("Nem : ");
        pressureLayout.addComponents(humidityLabel);

        windSpeedLabel = new Label("Ruzgar : ");
        pressureLayout.addComponents(windSpeedLabel);

        feelsLike = new Label("Hissedilen : ");
        pressureLayout.addComponents(feelsLike);


        mainDescriptionLayout.addComponents(destiptionlayout,pressureLayout);
        mainLayout.addComponents(mainDescriptionLayout);



    }

}
