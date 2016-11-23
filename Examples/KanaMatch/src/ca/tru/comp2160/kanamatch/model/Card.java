package ca.tru.comp2160.kanamatch.model;

public class Card {
	private String label;
	private String value;
	private boolean selected;
	
	public Card(String label) {
		this.setLabel(label);
		this.setValue(label);
	}
	
	public void setLabel(String label) { this.label = label; }
	public String getLabel() { return this.label; }

	public void setValue(String value) { this.value = value; }
	public String getValue() { return this.value; }

	public void setSelected(boolean selected) { this.selected = selected; }
	public boolean isSelected() { return this.selected; }
}
