package mtp.services;

import java.util.Collection;

import mtp.dataobjects.Entry;

public interface DatastoreService {

	public abstract void put(Entry entry);

	public abstract Collection<Entry> values();

}