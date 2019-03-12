package com.taf.automation.ui.support;

import com.ximpleware.AutoPilot;
import com.ximpleware.VTDGen;
import com.ximpleware.VTDNav;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This class uses VTD-XML for reading from an XML file. Use this class if having performance issues with the
 * XML class.
 */
public class VTD_XML {
    protected VTDGen vg;
    protected VTDNav vn;
    protected AutoPilot ap;

    // Used in navigation to determine if the parent node was selected
    protected boolean bSetNode = false;

    // Node list to the root xpath
    protected String[] toRootXpath;

    // Node list to each node
    protected String[][] toEachNode;

    /**
     * Needed for the TestNG unit test in this case.
     */
    public VTD_XML() {
    }

    /**
     * Constructor - Read XML from file
     *
     * @param sConfigFile - Location of XML file to parse
     * @throws Exception
     */
    public VTD_XML(String sConfigFile) throws Exception {
        // Open a file and read the content into a byte array
        File f = new File(sConfigFile);
        FileInputStream fis = new FileInputStream(f);
        byte[] b = new byte[(int) f.length()];
        fis.read(b);
        fis.close();
        init(b);
    }

    /**
     * Constructor - Read XML content from bytes
     *
     * @param b - XML content as bytes
     * @throws Exception
     */
    public VTD_XML(byte[] b) throws Exception {
        init(b);
    }

    /**
     * Constructor - Read XML content from an input stream
     *
     * @param in - Input Stream
     * @throws Exception
     */
    public VTD_XML(InputStream in) throws Exception {
        byte[] b = IOUtils.toByteArray(in);
        in.close();
        init(b);
    }

    /**
     * Initialization reading the XML content
     *
     * @param b - XML content as bytes
     * @throws Exception
     */
    protected void init(byte[] b) throws Exception {
        // Instantiate VTDGen and call parse
        vg = new VTDGen();
        vg.setDoc(b);
        vg.parse(true); // set namespace awareness to true

        // Retrieve the VTDNav object from VTDGen & instantiate the AutoPilot object
        vn = vg.getNav();
        ap = new AutoPilot(vn);
    }

    /**
     * Initialization required before use of method getNextNode() which is optimized parsing XML files up to
     * 2GB provided enough memory can be allocated.<BR>
     * <BR>
     * <p>
     * <B>Notes:</B><BR>
     * 1) On a 32-bit system, the max memory I was able to allocate was 1.6GB.<BR>
     * 2) On a 64-bit system, no memory allocation was need but application must be run from command line.
     * (This may be that eclipse was not running the 64-bit version of Java.)<BR>
     * <BR>
     * <B>Examples:</B><BR>
     * 1) Given following XML and you want to parse values for var1 &amp; var2<BR>
     * &lt;rootNode&gt;<BR>
     * &nbsp;&nbsp;&lt;testcase&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;var1&gt;1&lt;/var1&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;nested&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;var2&gt;2&lt;/var2&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/nested&gt;<BR>
     * &nbsp;&nbsp;&lt;/testcase&gt;<BR>
     * &nbsp;&nbsp;&lt;testcase&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;var1&gt;abc&lt;/var1&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;nested&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;var2&gt;def&lt;/var2&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/nested&gt;<BR>
     * &nbsp;&nbsp;&lt;/testcase&gt;<BR>
     * &lt;/rootNode&gt;<BR>
     * <BR>
     * Then, use sRootXpath = "/rootNode/testcase", eachNodesXpath[0] = "/var1", eachNodesXpath[1] =
     * "/nested/var2"<BR>
     *
     * @param sRootXpath     - the root xpath
     * @param eachNodesXpath - the xpath to each of the nodes to be stored in the array
     */
    public void initializeForOptimizedParsing(String sRootXpath, String[] eachNodesXpath) {
        /*
         * Break the root xpath into pieces for navigation by toElement
         */
        if (sRootXpath.startsWith("/")) {
            toRootXpath = sRootXpath.substring(1).split("/");
        } else {
            toRootXpath = sRootXpath.split("/");
        }

        /*
         * Break each node xpath into pieces for navigation by toElement
         */
        int nNodes = eachNodesXpath.length;
        toEachNode = new String[nNodes][getMaxArraySize(eachNodesXpath)];
        for (int i = 0; i < nNodes; i++) {
            if (eachNodesXpath[i].startsWith("/")) {
                toEachNode[i] = eachNodesXpath[i].substring(1).split("/");
            } else {
                toEachNode[i] = eachNodesXpath[i].split("/");
            }
        }
    }

    /**
     * Given an array of xpaths, the method determines which xpath is the largest. This can be used to
     * allocate an array to hold the values after parsing.
     *
     * @param nodes - array of xpaths
     * @return
     */
    protected int getMaxArraySize(String[] nodes) {
        /*
         * Step 1: Get the lengths of each array
         */
        int nLength = nodes.length;
        int[] arraySizes = new int[nLength];
        for (int i = 0; i < nLength; i++) {
            if (nodes[i].startsWith("/")) {
                arraySizes[i] = nodes[i].substring(1).split("/").length - 1;
            } else {
                arraySizes[i] = nodes[i].split("/").length - 1;
            }
        }

        /*
         * Step 2: Find the largest array size from the previous step
         */
        int nLargestArray = arraySizes[0];
        for (int i = 1; i < nLength; i++) {
            if (arraySizes[i] > nLargestArray) {
                nLargestArray = arraySizes[i];
            }
        }

        return nLargestArray;
    }

    /**
     * Gets the first nodes value for xpath as a String.<BR>
     * <BR>
     * <B>Note:</B><BR>
     * If node contains child nodes, then default value is returned.<BR>
     * <BR>
     * <B>Example:</B><BR>
     * Sample xml for examples below:<BR>
     * &nbsp;&nbsp;&lt;test&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;user 1&lt;/user&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
     * &nbsp;&nbsp;&lt;/test&gt;<BR>
     * <BR>
     * 1) //test returns default value<BR>
     * 2) //test/user returns "user 1"<BR>
     *
     * @param sXpath   - xpath to node
     * @param sDefault - value returned if exception occurs
     * @return String
     */
    public String getNodeValue(String sXpath, String sDefault) {
        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);

            // Evaluate the xpath which returns the node index
            int nNodeIndex = ap.evalXPath();

            // If valid index, continue to find value
            if (nNodeIndex != -1) {
                // Get the index of the value for this node
                int nValueIndex = vn.getText();

                // If valid index, return the value
                if (nValueIndex != -1) {
                    // return vn.toNormalizedString(nValueIndex);
                    return vn.toString(nValueIndex);
                }
            }
        } catch (Exception ex) {
        }

        return sDefault;
    }

    /**
     * Gets the first nodes value for xpath as an integer.<BR>
     * <BR>
     * <B>Note:</B><BR>
     * If node contains child nodes, then default value is returned.<BR>
     * <BR>
     * <B>Example:</B><BR>
     * Sample xml for examples below:<BR>
     * &nbsp;&nbsp;&lt;test&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;1&lt;/user&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
     * &nbsp;&nbsp;&lt;/test&gt;<BR>
     * <BR>
     * 1) //test returns default value<BR>
     * 2) //test/user returns 1<BR>
     *
     * @param sXpath   - xpath to node
     * @param nDefault - value returned if exception occurs
     * @return
     */
    public int getNodeValue(String sXpath, int nDefault) {
        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);

            // Evaluate the xpath which returns the node index
            int nNodeIndex = ap.evalXPath();

            // If valid index, continue to find value
            if (nNodeIndex != -1) {
                // Get the index of the value for this node
                int nValueIndex = vn.getText();

                // If valid index, return the value
                if (nValueIndex != -1) {
                    return Integer.parseInt(vn.toNormalizedString(nValueIndex));
                }
            }
        } catch (Exception ex) {
        }

        return nDefault;
    }

    /**
     * Gets the first nodes value for xpath as a boolean.<BR>
     * <BR>
     * <B>Note:</B><BR>
     * If node contains child nodes, then default value is returned.<BR>
     * <BR>
     * <B>Example:</B><BR>
     * Sample xml for examples below:<BR>
     * &nbsp;&nbsp;&lt;test&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;user&gt;true&lt;/user&gt;<BR>
     * &nbsp;&nbsp;&nbsp;&nbsp;&lt;password&gt;password 1&lt;/password&gt;<BR>
     * &nbsp;&nbsp;&lt;/test&gt;<BR>
     * <BR>
     * 1) //test returns default value<BR>
     * 2) //test/user returns true<BR>
     *
     * @param sXpath   - xpath to node
     * @param bDefault - value returned if exception occurs or no match to convert to boolean
     * @return
     */
    public boolean getNodeValue(String sXpath, boolean bDefault) {
        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);
            String sConvert = null;

            // Evaluate the xpath which returns the node index
            int nNodeIndex = ap.evalXPath();

            // If valid index, continue to find value
            if (nNodeIndex != -1) {
                // Get the index of the value for this node
                int nValueIndex = vn.getText();

                // If valid index, return the value
                if (nValueIndex != -1) {
                    sConvert = vn.toNormalizedString(nValueIndex);
                }
            }

            if (sConvert != null) {
                // Strings that will be considered to be true
                if (sConvert.equalsIgnoreCase("true")) {
                    return true;
                }

                if (sConvert.equalsIgnoreCase("1")) {
                    return true;
                }

                // Strings that will be considered to be false
                if (sConvert.equalsIgnoreCase("false")) {
                    return false;
                }

                if (sConvert.equalsIgnoreCase("0")) {
                    return false;
                }
            }
        } catch (Exception ex) {
        }

        return bDefault;
    }

    /**
     * Gets count of all Nodes for the given xpath.<BR>
     * <BR>
     * <B>Note:</B> For performance reasons, this result should be stored in a variable instead of calling the
     * function again if needed more than 1 time. As the processing time would be <B>n</B> instead of
     * <B>n</B>*<B>n</B>.
     *
     * @param sXpath - xpath for the nodes to count
     * @return
     */
    public int getNodesCount(String sXpath) {
        int nCount = 0;
        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);
            while (ap.evalXPath() != -1) {
                nCount++;
            }
        } catch (Exception ex) {
            return 0;
        }

        return nCount;
    }

    /**
     * Reads XML and puts the data into an array. Example, sRootXpath = "/root/testdata" &amp; sEachNode =
     * {"var1","var2"} =&gt; Get data for "/root/testdata[i]/var1" &amp; "/root/testdata[i]/var2" where i is for each
     * "/root/testdata" node found.
     *
     * @param sRootXpath - The root node that contains multiple sets of data
     * @param sEachNode  - All nodes that make up a specific set of data
     * @return String[][] with all data
     */
    public String[][] getAllData(String sRootXpath, String[] sEachNode) {
        // step 1: Get the number of nodes
        // step 2: Initialize the array that will hold the data
        // step 3: Use a loop to construct the unique xpath to node with data
        // step 4: Return the data
        int nNodes = getNodesCount(sRootXpath);
        String[][] data = new String[nNodes][sEachNode.length];
        for (int i = 0; i < nNodes; i++) {
            for (int j = 0; j < sEachNode.length; j++) {
                data[i][j] = getNodeValue(sRootXpath + "[" + (i + 1) + "]/" + sEachNode[j], null);
            }
        }

        return data;
    }

    /**
     * Gets the attribute to the specified node.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If xpath returns multiple nodes, only the first is checked for the attribute.<BR>
     *
     * @param sXpath     - xpath to node
     * @param sAttribute - attribute to find
     * @return null if cannot find
     */
    public String getAttribute(String sXpath, String sAttribute) {
        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);

            // Evaluate the xpath which returns the node index
            int nNodeIndex = ap.evalXPath();

            // If valid index, continue to find value
            if (nNodeIndex != -1) {
                // Does the specific node have the attribute?
                int nAttrIndex = vn.getAttrVal(sAttribute);
                if (nAttrIndex != -1) {
                    return vn.toString(nAttrIndex);
                }
            }
        } catch (Exception ex) {
        }

        return null;
    }

    /**
     * Gets the ID corresponding to the xpath if it exists else the same xpath is returned.
     *
     * @param sXpath
     * @return if ID exists it is returned else sXpath
     */
    public String getIDfromXpath(String sXpath) {
        String id = getAttribute(sXpath, "id");
        if (id == null) {
            return sXpath;
        } else {
            return id;
        }
    }

    /**
     * This method navigates to the next node and returns the data in an array using the method extractData()<BR>
     * <BR>
     * <B>Note: </B>If method cannot parse your XML file, then create a new class which inherits this class
     * and override this method &amp; the method extractData as necessary. (This may be the case if you need data
     * from attributes.)
     *
     * @return null if no more data else array of strings
     */
    public String[] getNextNode() {
        try {
            if (!bSetNode) {
                bSetNode = true;
                if (navigateToNode(toRootXpath, 1, VTDNav.FIRST_CHILD)) {
                    // Need to store state for later after getting data
                    vn.push();
                } else {
                    return null;
                }
            } else {
                // Restore previous state to get next sibling
                vn.pop();

                // Use the last node in toRootXpath, to go to the next sibling
                if (vn.toElement(VTDNav.NEXT_SIBLING, toRootXpath[toRootXpath.length - 1])) {
                    // Need to store state for later after getting data
                    vn.push();
                } else {
                    return null;
                }
            }

            return extractData();
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Navigates to the last node in the array starting from the specified node in the particular direction.
     *
     * @param nodes      - array of nodes to navigate through
     * @param nStartAt   - index of array node to start with
     * @param nDirection - Direction supported by VTD-XML
     * @return true if successfully navigated through the nodes else false
     */
    protected boolean navigateToNode(String[] nodes, int nStartAt, int nDirection) {
        try {
            for (int i = nStartAt; i < nodes.length; i++) {
                // If unable to navigate to the node, then return false which indicates an error
                if (!vn.toElement(nDirection, nodes[i])) {
                    return false;
                }
            }

            // Able to navigate to the node
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * This method reads the current node and returns it as an array<BR>
     * <BR>
     * <B>Note: </B>If method cannot parse your XML file, then create a new class which inherits this class
     * and override this method &amp; the method getNextNode as necessary. (This may be the case if you need data
     * from attributes.)
     *
     * @return String[]
     */
    protected String[] extractData() {
        try {
            String[] data = new String[toEachNode.length];

            int nValueIndex;
            for (int nNodes = 0; nNodes < toEachNode.length; nNodes++) {
                // Store state for later
                vn.push();

                // Assume no data
                data[nNodes] = null;

                // Need to go to the node & get the data
                if (navigateToNode(toEachNode[nNodes], 0, VTDNav.FIRST_CHILD)) {
                    // Get the index of the value for this node & use this value to get node value
                    nValueIndex = vn.getText();
                    if (nValueIndex != -1) {
                        data[nNodes] = vn.toString(nValueIndex);
                    }
                }

                // Restore state from before
                vn.pop();
            }

            return data;
        } catch (Exception ex) {
            return null;
        }
    }

    /**
     * Gets the attributes on the specified node.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) Parameter class is being re-used here. Parameter.param contains attribute to find. Parameter.value
     * contains default value if attribute is not found.<BR>
     *
     * @param sXpath     - xpath to node
     * @param attributes - List of attributes to find and default values if attribute is not found
     * @return List&lt;Parameter&gt; - if any error occurs then attributes list is returned
     */
    public List<Parameter> getAttribute(String sXpath, List<Parameter> attributes) {
        List<Parameter> data = new ArrayList<>();

        try {
            ap.resetXPath();
            ap.selectXPath(sXpath);

            // Evaluate the xpath which returns the node index
            int nNodeIndex = ap.evalXPath();

            // If valid index, continue to find attribute values
            if (nNodeIndex != -1) {
                for (Parameter p : attributes) {
                    // If attribute exists, then get value else use default
                    int nAttrIndex = vn.getAttrVal(p.param);
                    if (nAttrIndex != -1) {
                        data.add(new Parameter(p.param, vn.toString(nAttrIndex)));
                    } else {
                        data.add(new Parameter(p.param, p.value));
                    }
                }

                return data;
            } else {
                return attributes;
            }
        } catch (Exception ex) {
            return attributes;
        }
    }

    /**
     * Gets the attribute to the specified node.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If xpath returns multiple nodes, only the first is checked for the attribute.<BR>
     *
     * @param sXpath     - xpath to node
     * @param sAttribute - attribute to find
     * @param bDefault   - value returned if exception occurs or no match to convert to boolean
     * @return boolean
     */
    public boolean getAttribute(String sXpath, String sAttribute, boolean bDefault) {
        try {
            String value = getAttribute(sXpath, sAttribute);

            // Strings that will be considered to be true
            if (value.equalsIgnoreCase("true")) {
                return true;
            }

            if (value.equalsIgnoreCase("1")) {
                return true;
            }

            // Strings that will be considered to be false
            if (value.equalsIgnoreCase("false")) {
                return false;
            }

            if (value.equalsIgnoreCase("0")) {
                return false;
            }
        } catch (Exception ex) {
        }

        return bDefault;
    }

    /**
     * Gets the attribute to the specified node.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If xpath returns multiple nodes, only the first is checked for the attribute.<BR>
     *
     * @param sXpath     - xpath to node
     * @param sAttribute - attribute to find
     * @param nDefault   - value returned if exception occurs or cannot parse to integer
     * @return int
     */
    public int getAttribute(String sXpath, String sAttribute, int nDefault) {
        try {
            String value = getAttribute(sXpath, sAttribute);
            return Integer.parseInt(value);
        } catch (Exception ex) {
        }

        return nDefault;
    }

    /**
     * Gets the attribute to the specified node.<BR>
     * <BR>
     * <B>Notes:</B><BR>
     * 1) If xpath returns multiple nodes, only the first is checked for the attribute.<BR>
     *
     * @param sXpath     - xpath to node
     * @param sAttribute - attribute to find
     * @param sDefault   - value returned if xpath &amp; attribute combination returns null
     * @return String
     */
    public String getAttribute(String sXpath, String sAttribute, String sDefault) {
        String value = getAttribute(sXpath, sAttribute);
        if (value == null) {
            return sDefault;
        } else {
            return value;
        }
    }

}
