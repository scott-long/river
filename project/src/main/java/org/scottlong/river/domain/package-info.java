@XmlJavaTypeAdapters({
	@XmlJavaTypeAdapter(value = XmlTimestampAdapter.class, type = java.util.Date.class)
    })

package org.scottlong.river.domain;

import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapters;


