package io.oacy.java8FunctionPrograming.chapter2;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deno_02 {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		List<String> collected = Stream.of("a", "b", "c").collect(Collectors.toList()); 

		List<Track> tracks = Arrays.asList(new Track("Bakai", 524),new Track("Violets for Your Furs", 378),new Track("Time Was", 451));
				Track shortestTrack = tracks.stream()
				.min(Comparator.comparing(track -> track.getLength()))
				.get();
	}
	
	

}

class Track{
	@SuppressWarnings("unused")
	private String name;
	private int length;
	public Track() {
		
	}
	public Track(String name,int length) {
		this.name=name;
		this.length=length;
	}
	public int getLength() {
		return this.length;
	}
}
