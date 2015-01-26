package net.indiespot.struct.api;


public class StructConfig {
    //TODO commented out to remove dependency on agent
    //also, what was this for?
//	static {
//		String projectPackage = StructAgent.class.getPackage().getName();
//		for (Object key : System.getProperties().keySet()) {
//			String prop = (String) key;
//			if (!prop.startsWith(projectPackage))
//				continue;
//
//			// aight?
//		}
//	}


    static {
        SAFETY_FIRST = StructConfig.parseVmArg(StructConfig.class, "SAFETY_FIRST", 0, false) != 0L;
        PRINT_LOG = StructConfig.parseVmArg(StructConfig.class, "PRINT_LOG", 0, false) != 0L;
        MEMORY_BASE_OFFSET = StructConfig.parseVmArg(StructConfig.class, "MEMORY_BASE_OFFSET", 0, false) != 0L;
    }
	private static final String magnitude_lookup_table = "KMGT"; // kilo, mega,
																	// giga,
																	// tera


    public static final boolean SAFETY_FIRST;
    public static final boolean PRINT_LOG;
    public static final boolean MEMORY_BASE_OFFSET;

	public static final long parseVmArg(Class<?> type, String prop, long orElse, boolean asPOT) {
		String val = System.getProperty(type.getName() + "." + prop);
		if (val == null)
			val = System.getProperty("LibStruct." + prop);
		if (val == null || (val = val.trim()).isEmpty())
			return orElse;

		if (val.equalsIgnoreCase("false"))
			return 0L;
		if (val.equalsIgnoreCase("true"))
			return 1L;

		long magnitude = 1L;
		int magnitudeIndex = magnitude_lookup_table.indexOf(val.charAt(val.length() - 1));
		if (magnitudeIndex != -1) {
			for (int i = 0; i <= magnitudeIndex; i++)
				magnitude *= (asPOT ? 1024 : 1000);
			val = val.substring(0, val.length() - 1);
		}

		try {
			return Long.parseLong(val) * magnitude;
		} catch (Exception exc) {
			throw new IllegalArgumentException("failed to parse property '" + prop + "' with value '" + System.getProperty(prop) + "'");
		}
	}
}
