package org.hisp.dhis.program;

/*
 * Copyright (c) 2004-2017, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import org.hisp.dhis.analytics.AggregationType;
import org.hisp.dhis.common.BaseDimensionalItemObject;
import org.hisp.dhis.common.BaseIdentifiableObject;
import org.hisp.dhis.common.DimensionItemType;
import org.hisp.dhis.common.DxfNamespaces;
import org.hisp.dhis.common.IdScheme;
import org.hisp.dhis.common.IdentifiableObject;
import org.hisp.dhis.common.MergeMode;
import org.hisp.dhis.common.ValueType;
import org.hisp.dhis.legend.LegendSet;
import org.hisp.dhis.trackedentity.TrackedEntityAttribute;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hisp.dhis.common.DimensionalObjectUtils.COMPOSITE_DIM_OBJECT_PLAIN_SEP;

/**
 * @author Chau Thu Tran
 */
@JacksonXmlRootElement( localName = "programTrackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
public class ProgramTrackedEntityAttribute
    extends BaseDimensionalItemObject
{
    private Program program;

    private TrackedEntityAttribute attribute;

    private boolean displayInList;

    private Integer sortOrder;

    private Boolean mandatory;

    private Boolean allowFutureDate;

    private Set<ProgramTrackedEntityAttributeGroup> groups = new HashSet<>();

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public ProgramTrackedEntityAttribute()
    {
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute )
    {
        this();
        this.program = program;
        this.attribute = attribute;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory )
    {
        this( program, attribute );
        this.displayInList = displayInList;
        this.mandatory = mandatory;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory, Integer sortOrder )
    {
        this( program, attribute );
        this.displayInList = displayInList;
        this.mandatory = mandatory;
        this.sortOrder = sortOrder;
    }

    public ProgramTrackedEntityAttribute( Program program, TrackedEntityAttribute attribute, boolean displayInList,
        Boolean mandatory, Boolean allowFutureDate )
    {
        this( program, attribute, displayInList, mandatory );
        this.allowFutureDate = allowFutureDate;
    }

    // -------------------------------------------------------------------------
    // Logic
    // -------------------------------------------------------------------------

    public void addGroup( ProgramTrackedEntityAttributeGroup group )
    {
        groups.add( group );
        group.getAttributes().add( this );
    }

    public void removeGroup( ProgramTrackedEntityAttributeGroup group )
    {
        groups.remove( group );
        group.getAttributes().remove( this );
    }

    public void updateProgramTrackedEntityAttributeGroups( Set<ProgramTrackedEntityAttributeGroup> updates )
    {
        for ( ProgramTrackedEntityAttributeGroup group : new HashSet<>( groups ) )
        {
            if ( !updates.contains( group ) )
            {
                removeGroup( group );
            }
        }

        updates.forEach( this::addGroup );
    }

    @Override
    public String getName()
    {
        return (program != null ? program.getDisplayName() + " " : "") + (attribute != null ? attribute.getDisplayName() : "");
    }

    @Override
    public String getShortName()
    {
        return (program != null ? program.getDisplayShortName() + " " : "") + (attribute != null ? attribute.getDisplayShortName() : "");
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public ValueType getValueType()
    {
        return attribute != null ? attribute.getValueType() : null;
    }

    @Override
    public String toString()
    {
        return "{" +
            "\"class\":\"" + getClass() + "\", " +
            "\"id\":\"" + id + "\", " +
            "\"uid\":\"" + uid + "\", " +
            "\"program\":" + program + ", " +
            "\"attribute\":" + attribute + ", " +
            "\"created\":\"" + created + "\", " +
            "\"lastUpdated\":\"" + lastUpdated + "\" " +
            "}";
    }

    // -------------------------------------------------------------------------
    // DimensionalItemObject
    // -------------------------------------------------------------------------

    @Override
    public String getDimensionItem()
    {
        return (program != null ? program.getUid() : "") + COMPOSITE_DIM_OBJECT_PLAIN_SEP + (attribute != null ? attribute.getUid() : "");
    }

    @Override
    public String getDimensionItem( IdScheme idScheme )
    {
        return program.getPropertyValue( idScheme ) + COMPOSITE_DIM_OBJECT_PLAIN_SEP + attribute.getPropertyValue( idScheme );
    }

    @Override
    public DimensionItemType getDimensionItemType()
    {
        return DimensionItemType.PROGRAM_ATTRIBUTE;
    }

    @Override
    public List<LegendSet> getLegendSets()
    {
        return attribute != null ? attribute.getLegendSets() : null;
    }

    @Override
    public AggregationType getAggregationType()
    {
        return attribute != null ? attribute.getAggregationType() : null;
    }

    // -------------------------------------------------------------------------
    // Getters && Setters
    // -------------------------------------------------------------------------

    @JsonProperty
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Program getProgram()
    {
        return program;
    }

    public void setProgram( Program program )
    {
        this.program = program;
    }

    @JsonProperty( "trackedEntityAttribute" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "trackedEntityAttribute", namespace = DxfNamespaces.DXF_2_0 )
    public TrackedEntityAttribute getAttribute()
    {
        return attribute;
    }

    public void setAttribute( TrackedEntityAttribute attribute )
    {
        this.attribute = attribute;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory( Boolean mandatory )
    {
        this.mandatory = mandatory;
    }

    @JsonProperty
    @JacksonXmlProperty( localName = "displayInList", namespace = DxfNamespaces.DXF_2_0 )
    public boolean isDisplayInList()
    {
        return displayInList;
    }

    public void setDisplayInList( boolean displayInList )
    {
        this.displayInList = displayInList;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Boolean getAllowFutureDate()
    {
        return allowFutureDate;
    }

    public void setAllowFutureDate( Boolean allowFutureDate )
    {
        this.allowFutureDate = allowFutureDate;
    }

    @JsonProperty
    @JacksonXmlProperty( namespace = DxfNamespaces.DXF_2_0 )
    public Integer getSortOrder()
    {
        return sortOrder;
    }

    public void setSortOrder( Integer sortOrder )
    {
        this.sortOrder = sortOrder;
    }

    @JsonProperty( "programTrackedEntityAttributeGroups" )
    @JsonSerialize( as = BaseIdentifiableObject.class )
    @JacksonXmlProperty( localName = "programTrackedEntityAttributeGroups", namespace = DxfNamespaces.DXF_2_0 )
    public Set<ProgramTrackedEntityAttributeGroup> getGroups()
    {
        return this.groups;
    }

    public void setGroups( Set<ProgramTrackedEntityAttributeGroup> groups )
    {
        this.groups = groups;
    }

    @Override
    public void mergeWith( IdentifiableObject other, MergeMode mergeMode )
    {
        super.mergeWith( other, mergeMode );

        if ( other.getClass().isInstance( this ) )
        {
            ProgramTrackedEntityAttribute programTrackedEntityAttribute = (ProgramTrackedEntityAttribute) other;
            displayInList = programTrackedEntityAttribute.isDisplayInList();

            if ( mergeMode.isReplace() )
            {
                program = programTrackedEntityAttribute.getProgram();
                sortOrder = programTrackedEntityAttribute.getSortOrder();
                attribute = programTrackedEntityAttribute.getAttribute();
                mandatory = programTrackedEntityAttribute.isMandatory();
                allowFutureDate = programTrackedEntityAttribute.getAllowFutureDate();
            }
            else if ( mergeMode.isMerge() )
            {
                program = programTrackedEntityAttribute.getProgram() == null ? program : programTrackedEntityAttribute.getProgram();
                sortOrder = programTrackedEntityAttribute.getSortOrder() == null ? sortOrder : programTrackedEntityAttribute.getSortOrder();
                attribute = programTrackedEntityAttribute.getAttribute() == null ? attribute : programTrackedEntityAttribute.getAttribute();
                mandatory = programTrackedEntityAttribute.isMandatory() == null ? mandatory : programTrackedEntityAttribute.isMandatory();
                allowFutureDate = programTrackedEntityAttribute.getAllowFutureDate() == null ? allowFutureDate : programTrackedEntityAttribute.getAllowFutureDate();
            }

            groups.clear();

            for ( ProgramTrackedEntityAttributeGroup group : programTrackedEntityAttribute.getGroups() )
            {
                addGroup( group );
            }
        }
    }
}
