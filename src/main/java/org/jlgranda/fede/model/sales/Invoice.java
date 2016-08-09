/*
 * Copyright (C) 2015 jlgranda
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

import com.jlgranda.fede.ejb.sales.DocumentType;
import com.jlgranda.fede.ejb.sales.EmissionType;
import com.jlgranda.fede.ejb.sales.EnvironmentType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;
import org.jpapi.model.BussinesEntity;
import org.jpapi.util.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A Invoice document in fede
 * @author jlgranda
 */
@Entity
@Table(name = "INVOICE")
@DiscriminatorValue(value = "INV")
@PrimaryKeyJoinColumn(name = "invoiceId")
@NamedQueries({
    @NamedQuery(name = "Invoice.findAll", query = "SELECT i FROM Invoice i ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findByCodeAndCodeType", query = "SELECT i FROM Invoice i WHERE i.code = ?1 AND i.codeType = ?2 AND i.active=?3 ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findByDocumentType", query = "SELECT i FROM Invoice i WHERE i.documentType = ?1 AND i.active=?2 AND i.createdOn BETWEEN ?3 AND ?4 ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findByDocumentTypeAndAuthor", query = "SELECT i FROM Invoice i WHERE i.documentType = ?1 and i.author = ?2  AND i.active=?3 AND i.createdOn BETWEEN ?4 AND ?5 ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.findByDocumentTypeAndOwner", query = "SELECT i FROM Invoice i WHERE i.documentType = ?1 AND i.owner = ?2 AND i.active=?3 AND i.createdOn BETWEEN ?4 AND ?5 ORDER BY i.id DESC"),
    @NamedQuery(name = "Invoice.countByDocumentTypeAndOwner", query = "SELECT count(i) FROM Invoice i WHERE i.documentType = ?1 and i.owner = ?2 AND i.active=?3 AND i.createdOn BETWEEN ?4 AND ?5"),
    @NamedQuery(name = "Invoice.countByDocumentTypeAndAuthor", query = "SELECT count(i) FROM Invoice i WHERE i.documentType = ?1 and i.author = ?2 AND i.active=?3"),
})
public class Invoice extends BussinesEntity {
    
    private static Logger log = LoggerFactory.getLogger(Invoice.class);
    
    private static final long serialVersionUID = 2087202727290952436L;
    
    private EnvironmentType environmentType;
    
    private EmissionType emissionType;
    
    @ManyToOne(optional = true)
    @JoinColumn(name = "organization_id", insertable=false, updatable=false, nullable=true)
    private Organization organization;
    
    private DocumentType documentType;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "establishment_id", insertable=false, updatable=false, nullable=true)
    private Establishment establishment;
    
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "emissionpoint_id", insertable=false, updatable=false, nullable=true)
    private EmissionPoint emissionPoint;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<Detail> details = new ArrayList<>();
    
    private String sequencial;
    
    @OneToMany(cascade = {CascadeType.ALL}, mappedBy = "invoice", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();
    
    public EnvironmentType getEnvironmentType() {
        return environmentType;
    }

    public void setEnvironmentType(EnvironmentType environmentType) {
        this.environmentType = environmentType;
    }

    public EmissionType getEmissionType() {
        return emissionType;
    }

    public void setEmissionType(EmissionType emissionType) {
        this.emissionType = emissionType;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public DocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(DocumentType documentCode) {
        this.documentType = documentCode;
    }

    public Establishment getEstablishment() {
        return establishment;
    }

    public void setEstablishment(Establishment establishment) {
        this.establishment = establishment;
    }

    public EmissionPoint getEmissionPoint() {
        return emissionPoint;
    }

    public void setEmissionPoint(EmissionPoint emissionPoint) {
        this.emissionPoint = emissionPoint;
    }

    public Detail addDetail(Detail detail){
        detail.setInvoice(this);
        if (this.details.contains(detail)){
            replaceDetail(detail);
        } else {
            this.details.add(detail);
        }
        return detail;
    }
    
    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public String getSequencial() {
        return sequencial;
    }

    public void setSequencial(String sequencial) {
        this.sequencial = sequencial;
    }

    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }
    
    public Payment addPayment(Payment payment){
        payment.setInvoice(this);
        if (this.payments.contains(payment)){
            replacePayment(payment);
        } else {
            this.payments.add(payment);
        }
        return payment;
    }
    
    
    @Transient
    public String getSummary(){
        List<Detail> list = getDetails();
        Collections.sort(list);
        Collections.reverse(list);
        return Lists.toString(list);
    }
    
    @Transient
    public BigDecimal getTotal(){
        BigDecimal total = new BigDecimal(0);
        for (Detail d : getDetails()){
            total = total.add(d.getPrice().multiply(BigDecimal.valueOf(d.getAmount())));
        }
        
        return total;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.documentType);
        hash = 59 * hash + Objects.hashCode(this.emissionPoint);
        hash = 59 * hash + Objects.hashCode(this.sequencial);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Invoice other = (Invoice) obj;
        if (this.documentType != other.documentType) {
            return false;
        }
        if (!Objects.equals(this.emissionPoint, other.emissionPoint)) {
            return false;
        }
        if (!Objects.equals(this.sequencial, other.sequencial)) {
            return false;
        }
        return true;
    }

    public Detail replaceDetail(Detail detail) {
    
        getDetails().set(getDetails().indexOf(detail), detail);
        return detail;
    }
    
    public Payment replacePayment(Payment payment) {
    
        getPayments().set(getPayments().indexOf(payment), payment);
        return payment;
    }

}
