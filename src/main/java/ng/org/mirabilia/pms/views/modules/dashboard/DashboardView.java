package ng.org.mirabilia.pms.views.modules.dashboard;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.dom.Style;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import ng.org.mirabilia.pms.domain.entities.Property;
import ng.org.mirabilia.pms.domain.entities.User;
import ng.org.mirabilia.pms.domain.enums.PropertyType;
import ng.org.mirabilia.pms.domain.enums.Role;
import ng.org.mirabilia.pms.services.DashboardService;
import ng.org.mirabilia.pms.services.PropertyService;
import ng.org.mirabilia.pms.services.UserImageService;
import ng.org.mirabilia.pms.services.UserService;
import ng.org.mirabilia.pms.views.MainView;
import ng.org.mirabilia.pms.views.chart.ChartView;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.pekkam.Canvas;

import java.util.Arrays;
import java.util.List;

@Route(value = "", layout = MainView.class)
@PageTitle("Property Management System")
@RolesAllowed({"ADMIN","MANAGER","AGENT","ACCOUNTANT", "CLIENT", "IT_SUPPORT"})
@JsModule("chart.js")
@JsModule("https://cdn.jsdelivr.net/npm/chart.js")
public class DashboardView extends VerticalLayout {

    @Autowired
    DashboardService dashboardService;

    @Autowired
    UserService userService;

    @Autowired
    UserImageService userImageService;

    @Autowired
    PropertyService propertyService;

    Div layer1;

    Div layer2;

    Div layer3;
    Div layer4;
    Div layer5;
    Div layer6;
    @Autowired
    public DashboardView(DashboardService dashboardService, UserService userService, UserImageService userImageService, PropertyService propertyService) {
        setSpacing(true);
        setPadding(true);

        this.dashboardService = dashboardService;
        this.userService = userService;
        this.userImageService = userImageService;
        this.propertyService = propertyService;

        getStyle().setBackgroundColor("#F7F5F5");
        setDefaultHorizontalComponentAlignment(Alignment.STRETCH);

        ChartView chartView = new ChartView();
        chartView.setWidth("50%");
        chartView.setHeight("30vh");

        HorizontalLayout metricsLayout = createMetricsLayout();
        HorizontalLayout overviewAndCustomers = createOverviewAndCustomers();

        configureLayer1();
        configureLayer2();
        configureLayer3();
        configureLayer4();
        configureLayer5();
        configureLayer6();

        add(layer1, layer2, layer3, layer4, layer5, layer6);
    }

    void configureLayer1(){
        layer1 = new Div();
        H2 dashboard = new H2("Dashboard");
        layer1.add(dashboard);
    }
    void configureLayer2(){
        layer2 = new Div();
        layer2.getStyle().setBackgroundColor("white");
        layer2.getStyle().setBorderRadius("8px");
        layer2.getStyle().setPadding("8px");
        layer2.getStyle().setDisplay(Style.Display.FLEX);
        layer2.getStyle().setAlignItems(Style.AlignItems.CENTER);

        H4 filterBy = new H4("Filter by");

        ComboBox<PropertyType> comboBox = new ComboBox<>();
        comboBox.setItems(Arrays.stream(PropertyType.values()).toList());
        comboBox.getStyle()
                .set("opacity","0")
                .set("width","80px")
                .set("position", "absolute")
                .set("top", "0")  // Adjust position
                .set("left", "0px") // Adjust position
                .set("z-index", "10");

        /*// Create ComboStyleOverlay
        Div ovl = new Div();
        ovl.getStyle().setDisplay(Style.Display.FLEX);
        ovl.getStyle().setAlignItems(Style.AlignItems.CENTER);
        ovl.getStyle().setBorderRadius("8px");
        ovl.getStyle().set("border", "1px solid #1434A4");
        ovl.getStyle().setColor("blue");

        Image dropdown = new Image("/icons/arrowdown.png","");
        dropdown.setWidth("14px");
        dropdown.setHeight("8px");
        dropdown.getStyle().setMarginLeft("4px");

        H5 t = new H5("Property");

        ovl.add(t,dropdown);
        ovl.getStyle()
                .set("position", "absolute")
                .set("top", "0px")  // Adjust position
                .set("left", "0px") // Adjust position
                .set("z-index", "5"); // Ensure it's on top

        // Create a container Div
        Div container = new Div();
        container.getStyle()
                .setMarginLeft("15px")
                .set("position", "relative")
                .set("height","15px")
                .set("width", "200px");

        // Add ComboBox and View to the container
        container.add(comboBox, ovl);*/

        layer2.add(filterBy);

    }

    void configureLayer3(){
        layer3 = new Div();
        layer3.getStyle().setDisplay(Style.Display.FLEX);

        Div propertyCard = metricsCard("Properties for Sale","",
                String.valueOf(dashboardService.totalPropertiesForSale()),"");
        propertyCard.getStyle().setMarginRight("8px");
        Div residentialCard = metricsCard("Total Residential Properties","",
                String.valueOf(dashboardService.totalResidentialProperties()),"");
        residentialCard.getStyle().setMarginRight("8px");
        Div landedCard = metricsCard("Total Landed Properties","",
                String.valueOf(dashboardService.totalLandedProperties()),"");
        landedCard.getStyle().setMarginRight("8px");
        Div customerCard = metricsCard("Total Customers","",
                String.valueOf(dashboardService.totalCustomers()),"");

        layer3.add(propertyCard,residentialCard,landedCard,customerCard);
    }

    Div metricsCard(String label,String labelColor, String metric, String metricColor){
        H4 l = new H4(label);
        l.getStyle().setTextAlign(Style.TextAlign.CENTER);
        l.getStyle().setColor(labelColor);
        H1 m = new H1(metric);
        m.getStyle().setColor(metricColor);
        Div card = new Div(l,m);
        card.getStyle().setDisplay(Style.Display.FLEX);
        card.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);
        card.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);
        card.getStyle().setAlignItems(Style.AlignItems.CENTER);
        card.getStyle().setPadding("8px");
        card.getStyle().setBackgroundColor("white");
        card.getStyle().setWidth("25%");
        card.getStyle().setHeight("103px");
        card.getStyle().setBorderRadius("8px");
        return card;
    }

    void configureLayer4(){
        layer4 = new Div();
        layer4.getStyle().setDisplay(Style.Display.FLEX);
        layer4.setHeight("264px");

        Div left = new Div();
        left.setWidth("70%");
        left.getStyle().setBackgroundColor("white");
        left.getStyle().setBorderRadius("8px");
        left.add(layer4Graph());

        Div space = new Div();
        space.setWidth("8px");

        Div right = recentCustomerCard();
        right.setWidth("30%");

        layer4.add(left,space,right);
    }

    Div recentCustomerCard(){
        Div card = new Div();
        card.getStyle().setDisplay(Style.Display.FLEX);
        card.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);
        card.getStyle().setPadding("8px");
        card.getStyle().setBackgroundColor("white");
        card.getStyle().setBorderRadius("8px");

        H3 title = new H3("Recent Customers");
        title.getStyle().setFlexGrow("1");

        List<User> users = userService.getAllUsers();
        System.out.println("\n\n\nRecentCus\n\n" + users.size() + " \n\n\n");

        card.add(title);
        users.forEach((user)->{
            if(user.getRoles().contains(Role.CLIENT))
                card.add(customerCardEntryView(user));
        });

        Div footer = new Div();
        footer.getStyle().setDisplay(Style.Display.FLEX);
        footer.getStyle().setJustifyContent(Style.JustifyContent.END);
        footer.getStyle().setFlexGrow("1");

        Button viewAll = new Button("View all");
        viewAll.getStyle().setBackgroundColor("white");
        viewAll.getStyle().setAlignSelf(Style.AlignSelf.END);

        footer.add(viewAll);

        Div spacer = new Div();
        spacer.getStyle().setFlexGrow("2");

        card.add(spacer,footer);
        return card;
    }

    Div layer4Graph(){
        Div parent = new Div();
        parent.getStyle().setBorderRadius("8px");
        parent.setHeightFull();

        Div graphL1 = new Div();
        graphL1.setHeight("20%");
        graphL1.getStyle().setDisplay(Style.Display.FLEX);
        graphL1.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);
        graphL1.getStyle().setAlignItems(Style.AlignItems.CENTER);

        H5 title = new H5("Financial Overview");
        title.getStyle().setMarginLeft("8px");

        //Configure tabs
        Tabs tabs = new Tabs();
        tabs.getStyle().setMinHeight("0px");
        Tab daily = new Tab("Daily");
        Tab monthly = new Tab("Monthly");
        tabs.add(daily,monthly);
        tabs.addSelectedChangeListener((x)->{
            if(x.getSelectedTab().equals(daily)){
                renderChart("");
            } else if (x.getSelectedTab().equals(monthly)) {
                renderChart("");
            }
        });

        graphL1.add(title, tabs);

        Div graphL2 = new Div();
        graphL2.setHeight("10%");

        H6 caption = new H6("Total Revenue: ");
        caption.getStyle().setMarginLeft("8px");
        H3 revenue = new H3("$234,543");
        graphL2.getStyle().setDisplay(Style.Display.FLEX);
        graphL2.getStyle().setJustifyContent(Style.JustifyContent.START);
        graphL2.getStyle().setAlignItems(Style.AlignItems.CENTER);
        graphL2.add(caption, revenue);


        Div graphL3 = new Div();
        graphL3.setHeight("70%");
        //Configure chart canvas
        Canvas canvas = new Canvas(200,200);
        canvas.setId("canvasId");
        graphL3.add(canvas);
        graphL3.addAttachListener((x)->{
            System.out.println("\n\nCanvas Hello.....\n");
            renderChart("");
        });

        parent.add(graphL1,graphL2, graphL3);
        return parent;
    }
    void renderChart(String labels){
//        UI.getCurrent().getPage().executeJs("alert('in render chat')");
        String js = """
                const ctx = document.getElementById('canvasId').getContext('2d');
                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: ['01','02','03','04','05','06'],
                            datasets: [
                                {
                                    label: '# of Votes',
                                    data: [12, 19, 3, 5, 2, 3],
                                    borderWidth: 0.5,
                                    barThickness: 10,
                                    backgroundColor: '#1434A4',
                                    borderRadius: 10,
                                },
                                {
                                    label: 'o of Votes',
                                    data: [20, 20, 20, 20, 20, 20],
                                    borderWidth: 0.5,
                                    barThickness: 10,
                                    backgroundColor: '#D9D9D9',
                                    borderRadius: 10,
                                },
                                
                            ],
     
                        },
                        options: {
                            responsive: true,             
                            maintainAspectRatio: false,
                            scales: {
                                x: {
                                    stacked: true,
                                  
                                    border: {
                                        display: false,
                                    },
                                    grid: {
                                        display: false,
                                    }
                                },
                                y: {
                                    beginAtZero: true,
                                    stacked: false,
                                    border: {
                                        display: false,
                                    },
                                    grid: {
                                        display: false,
                                    }
                                   
                                },
                                
                            },
                            plugins: {
                                legend: {
                                    display: false
                                },
                            }
                           
                        }
                    });
                """;

        UI.getCurrent().getPage().executeJs(js);
    }
    void renderChartL5(String labels, String data1, String data2){
//        UI.getCurrent().getPage().executeJs("alert('in render chat')");
        String js = """
                const ctx = document.getElementById('canvasIdL6').getContext('2d');
                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels:""" +labels+
                            """
                            ,
                            datasets: [{
                                label: 'o of Votes',
                                data:""" + data1 +

                                """
                                ,
                                borderWidth: 0.5,
                                barThickness: 10,
                                backgroundColor: '#F4A74B',
                                borderRadius: 0,
                            },
                            {
                                label: '# of Votes',
                                data:""" + data2 +

                                """
                                ,
                                borderWidth: 0.5,
                                barThickness: 10,
                                backgroundColor: '#1434A4',
                                borderRadius: 0,
                            }
                            ],
     
                        },
                        options: {
                            responsive: true,             
                            maintainAspectRatio: false,
                            scales: {
                                x: {
                                 
                                    border: {
                                        display: false,
                                    },
                                    grid: {
                                        display: false,
                                    }
                                   
                                },
                                y: {
                                    beginAtZero: true,
                                    stacked: false,
                                    border: {
                                        display: false,
                                    },
                                    
                                    grid: {
                                        display: false,
                                    }
                                   
                                },
                                
                            },
                            plugins: {
                                legend: {
                                    display: false
                                },
                            }
                           
                        }
                    });
                """;

        UI.getCurrent().getPage().executeJs(js);
    }

    Div customerCardEntryView(User user){
        Div horizontal = new Div();
        horizontal.getStyle().setDisplay(Style.Display.FLEX);
        horizontal.getStyle().setAlignItems(Style.AlignItems.START);
        horizontal.getStyle().setMarginBottom("4px");

        Image image = new Image("/images/john.png","");
        image.setWidth("42px");
        image.setHeight("42px");
        image.getStyle().setBorderRadius("8px");
        image.getStyle().setMarginRight("8px");

        Div vertical = new Div();
        vertical.getStyle().setDisplay(Style.Display.FLEX);
        vertical.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);

        Span name = new Span(user.getFirstName());
        name.getStyle().setFontSize("16px");

        Span role = new Span("Owner");
        role.getStyle().setFontSize("10px");

        vertical.add(name,role);

        horizontal.add(image, vertical);
        return horizontal;
    }

    void configureLayer5(){
        layer5 = new Div();
        layer5.getStyle().setDisplay(Style.Display.FLEX);

        String totalPropertiesBought ="$"+ dashboardService.totalPropertiesBought();
        String totalRevenue = "$" + dashboardService.totalRevenue();
        String totalPaymentCompeted = "$"+dashboardService.totalPaymentCompleted();
        String totalOutStanding = "$" + dashboardService.totalPaymentOutstanding();

        Div boughtCard = metricsCard("Total Properties Bought","#2F2F2F",totalPropertiesBought,"#6C5DD3");
        boughtCard.getStyle().setMarginRight("8px");
        Div revenueCard = metricsCard("Total Revenue","#2F2F2F",totalRevenue,"#F4A74B");
        revenueCard.getStyle().setMarginRight("8px");
        Div paymentCard = metricsCard("Total Payments Completed","#2F2F2F",totalPaymentCompeted,"#F4A74B");
        paymentCard.getStyle().setMarginRight("8px");
        Div outstandingCard = metricsCard("Total Outstanding Payments","#2F2F2F",totalOutStanding,"#EB4444");

        layer5.add(boughtCard,revenueCard,paymentCard,outstandingCard);

    }

    void configureLayer6(){
        layer6 = new Div();
        layer6.getStyle().setMarginBottom("16px");
        layer6.getStyle().setDisplay(Style.Display.FLEX);

        Div left = new Div();
        left.setWidth("50%");
        left.setHeight("264px");
        left.getStyle().setBackgroundColor("white");
        left.getStyle().setBorderRadius("8px");
        left.add(layer6Graph());

        Div spacer = new Div();
        spacer.setWidth("8px");

        Div right = new Div();
        right.setWidth("50%");
        right.setHeight("264px");
        right.getStyle().setBackgroundColor("white");
        right.getStyle().setBorderRadius("8px");

        Div gridCard = constructGridCard();
        right.add(gridCard);

        layer6.add(left,spacer, right);
    }
    Div layer6Graph(){
        Div parent = new Div();
        parent.setWidthFull();
        parent.setHeightFull();

        Div layer1 = new Div();
        layer1.getStyle().setDisplay(Style.Display.FLEX);
        layer1.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);
        layer1.getStyle().setAlignItems(Style.AlignItems.CENTER);
        layer1.setWidthFull();
        layer1.setHeight("15%");

        H5 title = new H5("Property Overview");
        title.getStyle().setMarginLeft("8px");
        layer1.add(title);

        Div legends = new Div();
        legends.getStyle().setDisplay(Style.Display.FLEX);
        legends.getStyle().setFlexDirection(Style.FlexDirection.ROW);
        legends.getStyle().setJustifyContent(Style.JustifyContent.END);
        legends.getStyle().setAlignItems(Style.AlignItems.CENTER);
        legends.getStyle().setMarginRight("8px");

        Div c1 = new Div();
        c1.getStyle().setWidth("8px");
        c1.getStyle().setHeight("8px");
        c1.getStyle().setBorderRadius("4px");
        c1.getStyle().setBackgroundColor("#F4A74B");
        c1.getStyle().setMarginRight("8px");

        H6 c1Label = new H6("Land");

        Div c2 = new Div();
        c2.getStyle().setWidth("8px");
        c2.getStyle().setHeight("8px");
        c2.getStyle().setBorderRadius("4px");
        c2.getStyle().setBackgroundColor("blue");

        H6 c2Label = new H6("Apartment");

        Div spacer = new Div();
        spacer.setWidth("8px");

        legends.add(c1,c1Label,spacer,c2, c2Label);

        layer1.add(title, legends);

        Div graphL2 = new Div();
        graphL2.setHeight("85%");

        //Configure chart canvas
        Canvas canvas = new Canvas(200,200);
        canvas.setId("canvasIdL6");
        graphL2.add(canvas);
        graphL2.addAttachListener((x)->{
            System.out.println("\n\nCanvas Hello.....\n");
            renderChartL5(
                    "['April','May','June','July','August','September']",
                    "[12, 19, 3, 5, 2, 28]",
                    "[20,20,20,20,30,25]");
        });
        parent.add(layer1,graphL2);
        return parent;
    }


    Div constructGridCard(){
        Div card = new Div();
        card.setHeight("264px");
        card.getStyle().setDisplay(Style.Display.FLEX);
        card.getStyle().setFlexDirection(Style.FlexDirection.COLUMN);

        Div header = new Div();
        header.getStyle().setPaddingLeft("8px");
        header.getStyle().setPaddingRight("8px");
        header.getStyle().setDisplay(Style.Display.FLEX);
        header.getStyle().setFlexDirection(Style.FlexDirection.ROW);
        header.getStyle().setJustifyContent(Style.JustifyContent.SPACE_BETWEEN);
        header.getStyle().setHeight("15%");
        header.getStyle().setAlignItems(Style.AlignItems.CENTER);

        H5 propertyList = new H5("Property Listing");

        Button seeAll = new Button("See all");
        seeAll.getStyle().setBackgroundColor("white");

        header.add(propertyList, seeAll);

        Grid<Property> propertyGrid = new Grid<>(Property.class, false);
        propertyGrid.setHeight("85%");

        Grid.Column<Property> imageColumn = propertyGrid.addComponentColumn((property -> {
            return new Image("/images/john.png","");
        })).setHeader("");
        imageColumn.setWidth("20px");
        Grid.Column<Property> nameColumn = propertyGrid.addColumn(Property::getTitle)
                .setHeader("Name");
        nameColumn.setSortable(true);
        Grid.Column<Property> typeColumn = propertyGrid.addColumn((property)->property.getPropertyType().name())
                .setHeader("Type");
        Grid.Column<Property> locationColumn = propertyGrid.addColumn(Property::getStreet)
                .setHeader("Location");
        Grid.Column<Property> statusColumn = propertyGrid.addColumn(Property::getPropertyStatus)
                .setHeader("Status");
        Grid.Column<Property> priceColumn = propertyGrid.addColumn(Property::getPrice)
                .setHeader("Price");
        propertyGrid.setColumnOrder(imageColumn, nameColumn, locationColumn,statusColumn,typeColumn, priceColumn);
        propertyGrid.setItems(propertyService.getAllProperties());


        card.add(header, propertyGrid);
        return card;
    }
    private HorizontalLayout createMetricsLayout() {
        HorizontalLayout metricsLayout = new HorizontalLayout();
        metricsLayout.setWidthFull();
        metricsLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Div totalProperties = createMetricBox("Total Properties", "0", "#0000ff");
        Div totalLandProperties = createMetricBox("Total Land Properties", "0", "#00b140");
        Div totalResidentialProperties = createMetricBox("Total Residential Properties", "0", "#ffa500");
        Div totalClients = createMetricBox("Total Clients", "0", "#800080");
        Div totalStaff = createMetricBox("Total Staff", "0", "#808080");

        metricsLayout.add(totalProperties, totalLandProperties, totalResidentialProperties, totalClients, totalStaff);
        return metricsLayout;
    }
    private Div createMetricBox(String title, String value, String color) {
        Div box = new Div();
        box.addClassName("metric-box");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("font-size", "2rem").set("color", color).set("font-weight", "bold");

        Span titleSpan = new Span(title);
        titleSpan.getStyle().set("font-size", "1rem").set("color", "#5a5a5a");

        box.add(valueSpan, titleSpan);
        return box;
    }
    private HorizontalLayout createOverviewAndCustomers() {
        HorizontalLayout layout = new HorizontalLayout();
        layout.setWidthFull();
        layout.setSpacing(true);

        VerticalLayout financialOverview = new VerticalLayout();
        financialOverview.addClassName("financial-overview");

        Span overviewTitle = new Span("Financial Overview");
        overviewTitle.getStyle().set("font-weight", "bold").set("font-size", "1.25rem");
        financialOverview.add(overviewTitle, createFinancialOverview());

        VerticalLayout recentCustomers = new VerticalLayout();
        recentCustomers.addClassName("recent-customers");

        Span customersTitle = new Span("Recent Customers");
        customersTitle.getStyle().set("font-weight", "bold").set("font-size", "1.25rem");
        recentCustomers.add(customersTitle, new Span("View All"));

        layout.add(financialOverview, recentCustomers);
        return layout;
    }
    private VerticalLayout createFinancialOverview() {
        VerticalLayout overviewContent = new VerticalLayout();
        overviewContent.addClassName("financial-overview-content");

        Div totalBought = createFinancialBox("₦0", "Total Properties Bought", "#0000ff");
        Div totalOutstanding = createFinancialBox("₦0", "Total Outstanding Amount", "#ff0000");
        Div totalPaid = createFinancialBox("₦0", "Total Amount Paid", "#00b140");

        overviewContent.add(totalBought, totalOutstanding, totalPaid);
        return overviewContent;
    }
    private Div createFinancialBox(String value, String title, String color) {
        Div box = new Div();
        box.addClassName("financial-box");

        Span valueSpan = new Span(value);
        valueSpan.getStyle().set("font-size", "1.5rem").set("color", color).set("font-weight", "bold");

        Span titleSpan = new Span(title);
        titleSpan.getStyle().set("font-size", "1rem").set("color", "#5a5a5a");

        box.add(valueSpan, titleSpan);
        return box;
    }
}
