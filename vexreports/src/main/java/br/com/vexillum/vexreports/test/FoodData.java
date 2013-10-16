package br.com.vexillum.vexreports.test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FoodData {
	private static List<Food> foods = new ArrayList<Food>();
	static {
		foods.add(new Food("Vegetables", "Asparagus", "Vitamin K", 115, 43,
				"1 cup - 92 grams"));
		foods.add(new Food("Vegetables", "Beets", "Folate", 33, 74,
				"1 cup - 170 grams"));
		// other foods
	}

	public static List<Food> getFoodsByCategory(String category) {
		List<Food> somefoods = new ArrayList<Food>();
		for (Iterator<Food> i = foods.iterator(); i.hasNext();) {
			Food tmp = i.next();
			if (tmp.getCategory().equals(category))
				somefoods.add(tmp);
		}
		return somefoods;
	}
}
