package com.bean.diff;

public class DiffVO {
	private String id;
	private String beforeValue;
	private String afterValue;
	private ModType modType;
	private String jxPath;

	public String getJxPath() {
		return jxPath;
	}

	public void setJxPath(String jxPath) {
		this.jxPath = jxPath;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBeforeValue() {
		return beforeValue;
	}

	public void setBeforeValue(String beforeValue) {
		this.beforeValue = beforeValue;
	}

	public String getAfterValue() {
		return afterValue;
	}

	public void setAfterValue(String afterValue) {
		this.afterValue = afterValue;
	}

	public ModType getModType() {
		return modType;
	}

	public void setModType(ModType modType) {
		this.modType = modType;
	}

	public enum ModType {
		Modify("M"), Delete("D"), Add("A");

		private String value;

		ModType(String s) {
			this.value = s;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return this.value;
		}
	}

	@Override
	public String toString() {
		return this.id + " " + this.modType + " " + this.beforeValue + " " + this.afterValue +" "+this.jxPath;
	}
}
