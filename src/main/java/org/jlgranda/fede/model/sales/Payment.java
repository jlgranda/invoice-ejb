/*
 * Copyright (C) 2016 jlgranda
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package org.jlgranda.fede.model.sales;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jpapi.model.PersistentObject;

/**
 * Payment for a invoice
 * @author jlgranda
 */
@Entity
@Table(name = "PAYMENT")
public class Payment extends PersistentObject implements Comparable<Payment>, Serializable {

    private static final long serialVersionUID = -6685382197357879651L;

    @ManyToOne(optional = false, cascade = {CascadeType.ALL})
    @JoinColumn(name = "invoice_id", insertable=true, updatable=true, nullable=true)
    private Invoice invoice;
    
    private String method;
    /**
     * Monto del pago a registrar
     */
    private BigDecimal amount;
    
    /**
     * Descuento aplicado
     */
    private BigDecimal discount;
    
    /**
     * Cantidad entregada para realizar el pago
     */
    private BigDecimal cash;
    
    /**
     * Cambio a entregar
     */
    private BigDecimal change;

    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getCash() {
        return cash;
    }

    public void setCash(BigDecimal cash) {
        this.cash = cash;
    }

    public BigDecimal getChange() {
        return change;
    }

    public void setChange(BigDecimal change) {
        this.change = change;
    }

    
           
    @Override
    public int hashCode() {
        HashCodeBuilder hcb = new HashCodeBuilder(17, 31); // two randomly chosen prime numbers
        // if deriving: appendSuper(super.hashCode()).
        hcb.append(getInvoice()).
                    append(getId());

        return hcb.toHashCode();
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Detail other = (Detail) obj;
        EqualsBuilder eb = new EqualsBuilder();
        
        eb.append(getId(), other.getId()).
                    append(getInvoice(), other.getInvoice());
        return eb.isEquals();
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("")
        .append(getMethod())
        .append(": ")
        .append(getCreatedOn())
        .append("[")
        .append(getAmount())
        .append(", ")
        .append(getDiscount())
        .append(", ")
        .append(getCash())
        .append(", ")
        .append(getChange())
        .append("]");
        return str.toString();
    }
    
    @Override
    public int compareTo(Payment other) {
        return this.createdOn.compareTo(other.getCreatedOn());
    }
    
}
