package br.com.vexillum.vexreports.model;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Product {

	private Long id;
	private String productLine;
	private String item;
	private String state;
	private String branch;
	private Long quantity;
	private Float amount;
	private Code code = new Code();

    private Boolean showAsPrices;

	public static List statistics_ = new ArrayList();
	private static SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

	static String[] images = {"confused.gif","cool.gif","happy.gif","puaj.gif","ungry.gif","what.gif"};
	static int counter = 0;

	public List getStatistics(){
		return statistics_;
	}
	public List getEmptyStatistics(){
		return Collections.EMPTY_LIST;
	}

    public Product(){}

	public Product(Long id, String productLine, String item, String state, String branch, Long quantity, Float amount) {
		this.id = id;
		this.productLine = productLine;
		this.item = item;
		this.state = state;
		this.branch = branch;
		this.quantity = quantity;
		this.amount = amount;
    }

    public Product(Long id, String productLine, String item, String state, String branch, Long quantity, Float amount, boolean showAsPrices) {
        this(id, productLine, item, state, branch, quantity, amount);
        this.showAsPrices = showAsPrices;
    }

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Float getAmount() {
		return amount;
//		return new Float(quantity.floatValue());
	}

	public void setAmount(Float amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getProductLine() {
		return productLine;
	}

	public void setProductLine(String productLine) {
		this.productLine = productLine;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

    public Date getDate() {
        return new Date();
    }
    
    public Boolean getIsAvailable(){
    	return Boolean.valueOf(random.nextBoolean());
    }

    public Boolean getShowAsPrices() {
        return showAsPrices;
    }

    public void setShowAsPrices(Boolean showAsPrices) {
        this.showAsPrices = showAsPrices;
    }

    static Random random = new Random();

	public InputStream getImage() {
		InputStream ret = this.getClass().getClassLoader().getResourceAsStream("images/emoticons/" + images[counter++]);
		if (counter >= images.length)
			counter = 0;
		return ret;
	}
	
	public Code getCode() {
		return code;
	}	
	

	public class Code {
		
		public String getCode() {
			return "001-123ABC-HRC";
		}
	}
}