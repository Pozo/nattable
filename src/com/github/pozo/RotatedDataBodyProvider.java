package com.github.pozo;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.data.person.Person;

public class RotatedDataBodyProvider implements IDataProvider {
	private final List<Person> persons;

	public RotatedDataBodyProvider(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		if (rowIndex == 0) {
			return persons.get(columnIndex).getFirstName();
		} else if (rowIndex == 1) {
			return persons.get(columnIndex).getLastName();
		} else if (rowIndex == 2) {
			return persons.get(columnIndex).getGender();
		} else if (rowIndex == 3) {
			return persons.get(columnIndex).getBirthday();
		} else {
			return "";
		}
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		throw new UnsupportedOperationException();
		
	}

	@Override
	public int getColumnCount() {
		return persons.size();
	}

	@Override
	public int getRowCount() {
		return 4;
	}

}