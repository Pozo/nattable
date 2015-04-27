package com.github.pozo;

import java.util.List;

import org.eclipse.nebula.widgets.nattable.data.IDataProvider;
import org.eclipse.nebula.widgets.nattable.examples.data.person.Person;

public class RotatedDataColumnProvider implements IDataProvider {
	private final List<Person> persons;

	public RotatedDataColumnProvider(List<Person> persons) {
		this.persons = persons;
	}

	@Override
	public Object getDataValue(int columnIndex, int rowIndex) {
		return persons.get(columnIndex).getId();
	}

	@Override
	public void setDataValue(int columnIndex, int rowIndex, Object newValue) {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return persons.size();
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 1;
	}

}