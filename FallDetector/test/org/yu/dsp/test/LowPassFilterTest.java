package org.yu.dsp.test;

import static org.junit.Assert.*;

import org.heartwings.care.falldetect.LowPassFilter;
import org.junit.Test;


public class LowPassFilterTest {
	private LowPassFilter lpf;
	
	@Test
	public void testNextValue() {
		lpf = new LowPassFilter(40, 20);
		for(int i=0;i<100;i++)
			System.out.println(lpf.nextValue(i));
	}

}
