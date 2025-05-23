package com.dsc.plugins.crepe.graphquery.ontology;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public abstract class OntologyQuery implements Serializable {

    protected OntologyQueryFilter ontologyQueryFilter = null;

    private static OntologyQuery parseString(String s) {
        // example: (conj (IS_CLICKABLE true) (HAS_TEXT coffee))
        OntologyQuery query;
        s = s.trim();
        if (s.startsWith("(") && s.endsWith(")") && s.length() > 1) {
            // remove the outmost parenthesis
            s = s.substring(1, s.length() - 1);
            s = s.trim();

            // process the possible OntologyQueryFilter
            if (s.startsWith("argmin") || s.startsWith("argmax") || s.startsWith("exists")) {
                // contains an OntologyQueryFilter -- need to process
                int filterEndIndex = s.indexOf(" ", s.indexOf(" ") + 1);
                String ontologyFilterString = s.substring(0, filterEndIndex);
                String queryString = s.substring(filterEndIndex + 1);
                OntologyQueryFilter filter = OntologyQueryFilter.deserialize(ontologyFilterString);
                OntologyQuery resultQuery = OntologyQuery.parseString(queryString);
                resultQuery.setOntologyQueryFilter(filter);
                return resultQuery;
            }

            // example of a complex s: (conj (has_class_name "android.widget.TextView")
            // (has_package_name "com.android.settings") (right ( conj (has_text "xxx")
            // (has_package_name "com.android.settings") ))
            int spaceIndex = s.indexOf(' ');
            String firstWord = s.substring(0, spaceIndex);
            // firstWord: conj
            if (s.contains("(")) {
                if (firstWord.equals("placeholder")) {
                    OntologyQuery innerQuery = parseString(s.substring(spaceIndex + 1));
                    query = new PlaceholderOntologyQuery(innerQuery);
                } else {
                    CombinedOntologyQuery q = new CombinedOntologyQuery();
                    // nested relation
                    if (firstWord.equals("conj")) {
                        q.setSubRelation(CombinedOntologyQuery.RelationType.AND);
                    } else if (firstWord.equals("or")) {
                        q.setSubRelation(CombinedOntologyQuery.RelationType.OR);
                    } else {
                        q.setSubRelation(CombinedOntologyQuery.RelationType.PREV);
                        q.setQueryRelation(SugiliteRelation.getRelationFromString(firstWord));
                    }

                    Set<OntologyQuery> subQ = new HashSet<>();
                    // walk through the string and parse in the next level query strings recursively
                    int lastMatchIndex = spaceIndex + 1;
                    int counter = 0;
                    for (int i = spaceIndex + 1; i < s.length(); i++) {
                        if (s.charAt(i) == '(')
                            counter++;
                        else if (s.charAt(i) == ')')
                            counter--;

                        if (counter == 0) {
                            OntologyQuery sub_query = parseString(s.substring(lastMatchIndex, i + 1));
                            subQ.add(sub_query);
                            lastMatchIndex = i + 2;
                            i++;
                        }
                    }
                    q.setSubQueries(subQ);
                    query = q;
                }
            }

            else {
                // base case: simple relation
                // note: the object will never be an accessibility node info (since this is
                // directly from user)
                String predicateString = firstWord;
                String objectString = s.substring(spaceIndex + 1, s.length());
                LeafOntologyQuery q = new LeafOntologyQuery();

                q.setQueryFunction(SugiliteRelation.getRelationFromString(predicateString));
                Set<SugiliteEntity> oSet = new HashSet<SugiliteEntity>();
                if (objectString.equalsIgnoreCase("true")) {
                    SugiliteEntity<Boolean> o = new SugiliteEntity<Boolean>(-1, Boolean.class, true);
                    oSet.add(o);
                } else if (objectString.equalsIgnoreCase("false")) {
                    SugiliteEntity<Boolean> o = new SugiliteEntity<Boolean>(-1, Boolean.class, false);
                    oSet.add(o);
                } else if (isNumeric(objectString)) {
                    // if the object is a double number, then add it to the object set as a double
                    SugiliteEntity<Double> o = new SugiliteEntity<Double>(-1, Double.class,
                            Double.parseDouble(objectString));
                    oSet.add(o);
                } else {
                    SugiliteEntity<String> o = new SugiliteEntity<String>(-1, String.class,
                            OntologyQueryUtils.removeQuoteSigns(objectString));
                    oSet.add(o);
                }
                q.setObjectSet(oSet);
                query = q;
            }
            return query;
        }

        else {

            // malformed query
            return null;
        }
    }

    public static OntologyQuery deserialize(String queryString) {
        // example: (conj (hasColor red) (isChecked true))
        try {
            return parseString(queryString);
        } catch (Exception e) {
            e.printStackTrace();
            // throw new RuntimeException("Failed to deserialize an ontology query");
        }
        return null;
    }

    public void setOntologyQueryFilter(OntologyQueryFilter ontologyQueryFilter) {
        this.ontologyQueryFilter = ontologyQueryFilter;
    }

    public OntologyQueryFilter getOntologyQueryFilter() {
        return ontologyQueryFilter;
    }

    public abstract boolean overallQueryFunction(SugiliteEntity currNode, UISnapshot graph);

    public Set<SugiliteEntity> executeOn(UISnapshot graph) {

        Set<SugiliteEntity> results = new HashSet<SugiliteEntity>();
        // for each node in the graph, follow the if statements in notes
        // if it matches query, then add to results set

        for (SugiliteEntity s : graph.getSugiliteEntityIdSugiliteEntityMap().values()) {
            if (overallQueryFunction(s, graph)) {
                results.add(s);
            }
        }

        if (ontologyQueryFilter != null) {
            return ontologyQueryFilter.filter(results, graph);
        } else {
            return results;
        }
    }

    @Override
    public abstract String toString();

    @Override
    public abstract OntologyQuery clone();

    static class SubjectEntityObjectEntityPair {
        private SugiliteEntity subject = null;
        private SugiliteEntity object = null;

        public SubjectEntityObjectEntityPair(SugiliteEntity subject, SugiliteEntity object) {
            this.subject = subject;
            this.object = object;
        }

        public SugiliteEntity getSubject() {
            return subject;
        }

        public SugiliteEntity getObject() {
            return object;
        }

        public void setObject(SugiliteEntity object) {
            this.object = object;
        }

        public void setSubject(SugiliteEntity subject) {
            this.subject = subject;
        }
    }

    private static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?"); // match a number with optional '-' and decimal.
    }
}
