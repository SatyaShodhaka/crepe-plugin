package com.dsc.plugins.crepe.graphquery.ontology;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by nancyli on 9/27/17.
 */

@Deprecated
public class SerializableOntologyQuery implements Serializable {
    private CombinedOntologyQuery.RelationType SubRelation;
    private Set<SerializableOntologyQuery> SubQueries = null;
    private SugiliteRelation r = null;
    private Set<SugiliteSerializableEntity> object = null;
    private Set<SugiliteSerializableEntity> subject = null;
    private OntologyQueryFilter ontologyQueryFilter = null;

    // public SerializableOntologyQuery(OntologyQueryWithSubQueries q){
    // r = q.getR();
    // SubRelation = q.getSubRelation();
    // ontologyQueryFilter = q.getOntologyQueryFilter();
    // if(SubRelation != OntologyQueryWithSubQueries.RelationType.nullR) {
    // // for nested query, recursively construct SerializableOntologyQuery
    // SubQueries = new HashSet<SerializableOntologyQuery>();
    // Set<OntologyQuery> pSubQueries = q.getSubQueries();
    // for (OntologyQuery pq : pSubQueries) {
    // SubQueries.add(new SerializableOntologyQuery(pq));
    // }
    // }
    // else{
    // // otherwise, transform SugiliteEntity to SugiliteSerializableEntity
    // Set<SugiliteEntity> po = q.getObjectSet();
    // Set<SugiliteEntity> ps = q.getSubjectSet();
    // if(po != null){
    // object = new HashSet<SugiliteSerializableEntity>();
    // for(SugiliteEntity o : po) {
    // object.add(new SugiliteSerializableEntity(o));
    // }
    // }
    //
    // if(ps != null){
    // subject = new HashSet<SugiliteSerializableEntity>();
    // for(SugiliteEntity s: ps){
    // subject.add(new SugiliteSerializableEntity(s));
    // }
    // }
    // }
    // }

    class SerializableSubjectEntityObjectEntityPair {
        private SugiliteSerializableEntity subject = null;
        private SugiliteSerializableEntity object = null;

        public SerializableSubjectEntityObjectEntityPair(OntologyQuery.SubjectEntityObjectEntityPair p) {
            subject = new SugiliteSerializableEntity(p.getSubject());
            object = new SugiliteSerializableEntity(p.getObject());
        }
    }

    public CombinedOntologyQuery.RelationType getSubRelation() {
        return SubRelation;
    }

    public void setSubRelation(CombinedOntologyQuery.RelationType subRelation) {
        SubRelation = subRelation;
    }

    public Set<SerializableOntologyQuery> getSubQueries() {
        return SubQueries;
    }

    public Set<SugiliteSerializableEntity> getObject() {
        return object;
    }

    public Set<SugiliteSerializableEntity> getSubject() {
        return subject;
    }

    public SugiliteRelation getR() {
        return r;
    }

    public OntologyQueryFilter getOntologyQueryFilter() {
        return ontologyQueryFilter;
    }

    @Override
    public String toString() {
        return new CombinedOntologyQuery(this).toString();
    }
}
