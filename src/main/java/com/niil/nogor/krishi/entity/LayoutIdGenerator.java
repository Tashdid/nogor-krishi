package com.niil.nogor.krishi.entity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.id.IdentifierGenerator;

/**
 * @author Noor
 * @email niildu@gmail.com
 * @since Jun 3, 2018
 *
 */
public class LayoutIdGenerator implements IdentifierGenerator {

	@Override
	public Serializable generate(SessionImplementor arg0, Object arg1) throws HibernateException {
		return Long.valueOf(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date()) + "" + (long)(Math.random() * 100 + 1));
	}

}
