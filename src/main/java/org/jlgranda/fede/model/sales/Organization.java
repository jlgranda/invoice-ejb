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

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import org.jpapi.model.BussinesEntity;

/**
 *
 * @author jlgranda
 */
@Entity
@Table(name = "Organization")
@DiscriminatorValue(value = "ORG")
@PrimaryKeyJoinColumn(name = "id")
@NamedQueries({
    @NamedQuery(name = "Organization.findByOwner", query = "select o FROM Organization o WHERE o.owner=?1 ORDER BY o.name ASC"),
    @NamedQuery(name = "Organization.countByOwner", query = "select count(o) FROM Organization o WHERE o.owner = ?1"),
})
public class Organization extends BussinesEntity implements Serializable {

    private static final long serialVersionUID = 3095488521256724258L;
    private String ruc;
    private String initials;
    
    public enum Type {
        GOVERMENT,
        PUBLIC,
        PRIVATE,
        NATURAL;
        private Type() {
        }
    }
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Organization.Type organizationType;

    public Type getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(Type organizationType) {
        this.organizationType = organizationType;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    @Override
    public String getCanonicalPath() {
        StringBuilder path = new StringBuilder();
        path.append(getName());
        return path.toString();
    }

    @Override
    public String toString() {
        return getName();
    }
}
