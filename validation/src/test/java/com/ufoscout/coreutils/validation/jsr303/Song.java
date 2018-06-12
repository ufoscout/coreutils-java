/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ufoscout.coreutils.validation.jsr303;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 
 * <class_description>
 * <p><b>notes</b>:
 * <p>ON : Feb 27, 2013
 *
 * @author Francesco Cina
 * @version $Revision
 */
public class Song {

	private Long id;
	private Long lyricId;

	@NotNull(message="notNull")
	@Size(min = 3, message="minLenght3")
	private String title;

	@NotNull(message="notNull")
	@Size(min = 3, message="minLenght3")
	private String artist;
	//		@Size(min = 4, message="minLenght4")
	@Min(value=1900, message="minSize1900")
	private Integer year;

	public Long getId() {
		return id;
	}

	public void setId(final Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(final String title) {
		this.title = title;
	}

	public String getArtist() {
		return artist;
	}

	public void setArtist(final String artist) {
		this.artist = artist;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(final Integer year) {
		this.year = year;
	}

	public Long getLyricId() {
		return lyricId;
	}

	public void setLyricId(final Long lyricId) {
		this.lyricId = lyricId;
	}

}
