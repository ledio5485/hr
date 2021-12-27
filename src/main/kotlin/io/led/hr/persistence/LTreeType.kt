package io.led.hr.persistence

import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.usertype.UserType
import java.io.Serializable
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Types


class LTreeType : UserType {
    override fun sqlTypes(): IntArray {
        return intArrayOf(Types.OTHER)
    }

    override fun returnedClass(): Class<*> {
        return String::class.java
    }

    @Throws(HibernateException::class)
    override fun equals(x: Any, y: Any): Boolean {
        return x == y
    }

    @Throws(HibernateException::class)
    override fun hashCode(x: Any): Int {
        return x.hashCode()
    }

    @Throws(HibernateException::class)
    override fun deepCopy(value: Any?): Any? {
        if (value == null) return null
        check(value is String) { "Expected String, but got: " + value.javaClass }
        return value
    }

    override fun isMutable(): Boolean {
        return false
    }

    @Throws(HibernateException::class)
    override fun disassemble(value: Any?): Serializable? {
        return value as Serializable?
    }

    @Throws(HibernateException::class)
    override fun assemble(cached: Serializable?, owner: Any?): Any? {
        return cached
    }

    @Throws(HibernateException::class)
    override fun replace(original: Any?, target: Any?, owner: Any?): Any? {
        return deepCopy(original)
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeGet(
        rs: ResultSet,
        names: Array<String?>,
        session: SharedSessionContractImplementor?,
        owner: Any?
    ): Any? {
        return rs.getString(names[0])
    }

    @Throws(HibernateException::class, SQLException::class)
    override fun nullSafeSet(
        st: PreparedStatement,
        value: Any?,
        index: Int,
        session: SharedSessionContractImplementor?
    ) {
        st.setObject(index, value, Types.OTHER)
    }
}