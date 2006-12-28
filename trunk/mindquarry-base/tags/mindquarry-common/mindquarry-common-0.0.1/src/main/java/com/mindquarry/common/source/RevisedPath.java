package com.mindquarry.common.source;


public class RevisedPath {
	private static final String ADDED = "addition";
	private static final String UNKNOWN = null;
	private static final String DELETED = "deletion";
	private static final String REPLACED = "replacement";
	private static final String MODIFIED = "modification";
	private final String path_;
	private final String action_;
	
	public RevisedPath(String path, char action) {
		this.path_ = path;
		switch (action) {
		case 'A':
			this.action_ = ADDED;
			break;
		case 'D':
				this.action_ = DELETED;
				break;
		case 'R':
			this.action_ = REPLACED;
			break;
		case 'M':
			this.action_ = MODIFIED;
			break;
		default:
			this.action_ = UNKNOWN;
			break;
		}
	}
	
	public String getAction() {
		return action_;
	}
	public String getPath() {
		return path_;
	}
}