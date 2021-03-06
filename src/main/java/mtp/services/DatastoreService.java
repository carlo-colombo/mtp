package mtp.services;

import java.util.Collection;

import mtp.dataobjects.Entry;

public interface DatastoreService {

	public abstract String put(Entry entry);

	public abstract Collection<Entry> list();

	public abstract Object getView(String viewName, Boolean group,
			Integer groupLevel);

}