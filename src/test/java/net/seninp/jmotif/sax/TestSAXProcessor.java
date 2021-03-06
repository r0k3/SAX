package net.seninp.jmotif.sax;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.ArrayList;
import net.seninp.jmotif.sax.alphabet.Alphabet;
import net.seninp.jmotif.sax.alphabet.NormalAlphabet;
import net.seninp.jmotif.sax.datastructures.SAXRecords;
import org.junit.Test;

/**
 * Test SAX factory methods.
 * 
 * @author Pavel Senin
 * 
 */
public class TestSAXProcessor {

  private static final String ts1File = "src/resources/test-data/timeseries01.csv";
  private static final String ts2File = "src/resources/test-data/timeseries02.csv";
  private static final String ts3File = "src/resources/test-data/timeseries03.csv";

  private static final String ts1StrRep10 = "bcjkiheebb";
  private static final String ts2StrRep10 = "bcefgijkdb";

  private static final String ts1StrRep14 = "bcdijjhgfeecbb";
  private static final String ts2StrRep14 = "bbdeeffhijjfbb";

  private static final String ts1StrRep7 = "bcggfddba";
  private static final String ts2StrRep7 = "accdefgda";

  private static final int length = 15;
  private static final int strLength = 10;

  private static final Alphabet normalA = new NormalAlphabet();

  private static final double delta = 0.001;

  /**
   * Testing the concatenated time series SAX conversion.
   * 
   * @throws NumberFormatException if error occurs.
   * @throws IOException if error occurs.
   * @throws SAXException if error occurs.
   */
  @Test
  public void testConnectedConversion() throws NumberFormatException, IOException, SAXException {

    SAXProcessor sp = new SAXProcessor();
    double[] ts = TSProcessor.readFileColumn(ts3File, 0, 0);

    ArrayList<Integer> skips = new ArrayList<Integer>();
    for (int i = 30 - 6; i < 30; i++) {
      skips.add(i);
    }

    SAXRecords regularSAX = sp.ts2saxViaWindow(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.NONE, 0.01);
    System.out.println("NONE: there are " + regularSAX.getAllIndices().size() + " words: \n"
        + regularSAX.getSAXString(" ") + "\n" + regularSAX.getAllIndices());
    SAXRecords saxData = sp.ts2saxViaWindowSkipping(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.NONE, 0.01, skips);
    System.out.println("NONE with skips: there are " + saxData.getAllIndices().size()
        + " words: \n" + saxData.getSAXString(" ") + "\n" + saxData.getAllIndices());

    regularSAX = sp.ts2saxViaWindow(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.EXACT, 0.01);
    assertNotNull("asserting the processing result", regularSAX);
    System.out.println("EXACT: there are " + regularSAX.getAllIndices().size() + " words: \n"
        + regularSAX.getSAXString(" ") + "\n" + regularSAX.getAllIndices());
    saxData = sp.ts2saxViaWindowSkipping(ts, 6, 3, normalA.getCuts(3),
        NumerosityReductionStrategy.EXACT, 0.01, skips);
    System.out.println("EXACT with skips: there are " + saxData.getAllIndices().size()
        + " words: \n" + saxData.getSAXString(" ") + "\n" + saxData.getAllIndices());
  }

  /**
   * Test the SAX conversion.
   * 
   * @throws Exception if error occurs.
   */
  @Test
  public void testTs2string() throws Exception {

    SAXProcessor sp = new SAXProcessor();

    double[] ts1 = TSProcessor.readFileColumn(ts1File, 0, length);
    double[] ts2 = TSProcessor.readFileColumn(ts2File, 0, length);

    // series #1 based test
    String ts1sax = sp.ts2saxByChunking(ts1, 10, normalA.getCuts(11), 0.005).getSAXString("");
    assertEquals("testing SAX", strLength, ts1sax.length());
    assertTrue("testing SAX", ts1StrRep10.equalsIgnoreCase(ts1sax));

    // ts1sax = SAXProcessor.ts2string(ts1, 14, normalA, 10);
    // assertEquals("testing SAX", 14, ts1sax.length());
    // assertTrue("testing SAX", ts1StrRep14.equalsIgnoreCase(ts1sax));
    //
    // ts1sax = SAXProcessor.ts2string(ts1, 9, normalA, 7);
    // assertEquals("testing SAX", 9, ts1sax.length());
    // assertTrue("testing SAX", ts1StrRep7.equalsIgnoreCase(ts1sax));
    //
    // // series #2 goes here
    // String ts2sax = SAXProcessor.ts2string(ts2, 10, normalA, 11);
    // assertEquals("testing SAX", strLength, ts2sax.length());
    // assertTrue("testing SAX", ts2StrRep10.equalsIgnoreCase(ts2sax));
    //
    // ts2sax = SAXProcessor.ts2string(ts2, 14, normalA, 10);
    // assertEquals("testing SAX", 14, ts2sax.length());
    // assertTrue("testing SAX", ts2StrRep14.equalsIgnoreCase(ts2sax));
    //
    // ts2sax = SAXProcessor.ts2string(ts2, 9, normalA, 7);
    // assertEquals("testing SAX", 9, ts2sax.length());
    // assertTrue("testing SAX", ts2StrRep7.equalsIgnoreCase(ts2sax));
  }

  // /**
  // * Test the distance function.
  // *
  // * @throws Exception if error occur.
  // */
  // @Test
  // public void testTs2sax() throws Exception {
  // ts1 = TSProcessor.readTS(ts1File, length);
  // ts2 = TSProcessor.readTS(ts2File, length);
  //
  // String ts2str_0 = SAXProcessor.ts2string(ts2.subsection(0, 4), 5, normalA, 10);
  // String ts2str_3 = SAXProcessor.ts2string(ts2.subsection(3, 7), 5, normalA, 10);
  // String ts2str_7 = SAXProcessor.ts2string(ts2.subsection(7, 11), 5, normalA, 10);
  //
  // SAXRecords ts2SAX = SAXProcessor.ts2saxZNorm(TSProcessor.zNormalize(ts2), 5, 5, normalA, 10);
  //
  // assertEquals("Testing ts2saxOptimized", ts2.size() - 5 + 1, ts2SAX.size());
  //
  // assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_0));
  // assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_3));
  // assertNotNull("Testing ts2sax", ts2SAX.getByWord(ts2str_7));
  //
  // assertSame("Testing ts2sax", ts2SAX.getByWord(ts2str_0).getIndexes().get(0), 0);
  // assertSame("Testing ts2sax", ts2SAX.getByWord(ts2str_3).getIndexes().get(0), 3);
  // assertSame("Testing ts2sax", ts2SAX.getByWord(ts2str_7).getIndexes().get(0), 7);
  //
  // }
  //
  // /**
  // * Test the distance function.
  // *
  // * @throws TSException if error occurs.
  // */
  // @Test
  // public void testStringDistance() throws TSException {
  // assertEquals("testing SAX distance", 0,
  // SAXProcessor.strDistance(new char[] { 'a' }, new char[] { 'b' }));
  // assertEquals("testing SAX distance", 2,
  // SAXProcessor.strDistance(new char[] { 'a', 'a', 'a' }, new char[] { 'b', 'c', 'b' }));
  //
  // Alphabet a = new NormalAlphabet();
  // assertEquals(
  // "testing SAX distance",
  // 0.861D,
  // SAXProcessor.saxMinDist(new char[] { 'a', 'a', 'a' }, new char[] { 'b', 'c', 'b' },
  // a.getDistanceMatrix(3)), delta);
  //
  // assertEquals(
  // "testing SAX distance",
  // 0.0D,
  // SAXProcessor.saxMinDist(new char[] { 'a', 'a', 'a' }, new char[] { 'b', 'b', 'b' },
  // a.getDistanceMatrix(3)), delta);
  // }
  //
  // /**
  // * Test the SAX conversion when NaN's presented in the timeseries.
  // *
  // * @throws Exception if error occurs.
  // */
  // @Test
  // public void testTs2stringWithNAN() throws Exception {
  //
  // // read the timeseries first
  // ts1 = TSProcessor.readTS(ts1File, length);
  // ts2 = TSProcessor.readTS(ts2File, length);
  //
  // // series #1 based test
  // String ts1sax = SAXProcessor.ts2string(ts1, 10, normalA, 11);
  // assertEquals("testing SAX", strLength, ts1sax.length());
  // assertTrue("testing SAX", ts1StrRep10.equalsIgnoreCase(ts1sax));
  //
  // Timeseries ts1WithNaN = new Timeseries(ts1.values(), ts1.tstamps(), ts1.values()[2]);
  // String tsWithNaN1sax = String.valueOf(TSProcessor.ts2StringWithNaNByCuts(ts1WithNaN,
  // normalA.getCuts(11)));
  // assertSame("Checking Z conversion", tsWithNaN1sax.charAt(2), '_');
  // }
  //
  // /**
  // * Test the SAX conversion when NaN's presented in the timeseries.
  // *
  // * @throws Exception if error occurs.
  // */
  // @Test
  // public void testTs2saxZnormByCuts() throws Exception {
  // //
  // // get series
  // ts1 = TSProcessor.readTS(ts1File, length);
  // ts2 = TSProcessor.readTS(ts2File, length);
  //
  // //
  // // convert to sax with 2 letters alphabet and internal normalization
  //
  // double[] cut = { 0.0D };
  // SAXRecords sax = SAXProcessor.ts2saxZnormByCuts(ts1, 14, 10, cut);
  // Iterator<SaxRecord> i = sax.iterator();
  // SaxRecord entry0 = i.next();
  // assertTrue("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase("aabbbbaaaa"));
  //
  // sax = SAXProcessor.ts2saxZnormByCuts(ts1, 2, 2, cut);
  // i = sax.iterator();
  // entry0 = i.next();
  // SaxRecord entry1 = i.next();
  // assertFalse("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase(String.valueOf(entry1.getPayload())));
  //
  // // test double[] version here
  // double[] data = new double[ts1.size()];
  // for (int k = 0; k < data.length; k++) {
  // data[k] = ts1.elementAt(k).value();
  // }
  //
  // sax = SAXProcessor.ts2saxZnormByCuts(data, 14, 10, cut);
  // i = sax.iterator();
  // entry0 = i.next();
  // assertTrue("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase("aabbbbaaaa"));
  //
  // }
  //
  // /**
  // * Test the SAX conversion when NaN's presented in the timeseries.
  // *
  // * @throws Exception if error occurs.
  // */
  // @Test
  // public void testTs2saxNoZnormByCuts() throws Exception {
  // //
  // // get series
  // ts1 = TSProcessor.readTS(ts1File, length);
  // ts2 = TSProcessor.readTS(ts2File, length);
  //
  // //
  // // convert to sax with 2 letters alphabet and internal normalization
  // double[] cut = { 0.0D };
  // SAXRecords sax = SAXProcessor.ts2saxNoZnormByCuts(ts1, 14, 10, cut);
  // Iterator<SaxRecord> i = sax.iterator();
  // SaxRecord entry0 = i.next();
  // assertTrue("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase("bbbbbbbbbb"));
  //
  // //
  // // now add two negatives
  // ts1.elementAt(5).setValue(-5.0D);
  // ts1.elementAt(4).setValue(-5.0D);
  // sax = SAXProcessor.ts2saxNoZnormByCuts(ts1, 14, 10, cut);
  // i = sax.iterator();
  // entry0 = i.next();
  // assertTrue("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase("bbbabbbbbb"));
  //
  // //
  // //
  // sax = SAXProcessor.ts2saxNoZnormByCuts(ts1, 2, 2, cut);
  // i = sax.iterator();
  // entry0 = i.next();
  // SaxRecord entry1 = i.next();
  // assertFalse("Testing SAX routines",
  // String.valueOf(entry0.getPayload()).equalsIgnoreCase(String.valueOf(entry1.getPayload())));
  // }
  //
  // /**
  // * Test the distance function (between strings).
  // */
  // @Test
  // public void testStrDistance() {
  // assertEquals("Testing StrDistance", 1, SAXProcessor.charDistance('a', 'b'));
  // assertEquals("Testing StrDistance", 5, SAXProcessor.charDistance('a', 'f'));
  // }

}
