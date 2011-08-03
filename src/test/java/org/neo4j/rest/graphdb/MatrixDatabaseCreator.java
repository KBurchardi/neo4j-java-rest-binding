package org.neo4j.rest.graphdb;

import java.io.File;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * Creates a database using the matrix example for testing purposes
 * @author Klemens Burchardi
 * @since 03.08.11
 */
public class MatrixDatabaseCreator {
	 /** specify relationship types*/
	 public enum RelTypes implements RelationshipType{
	     NEO_NODE,
	     KNOWS,
	     CODED_BY
	 }
	 
	 private static final String MATRIX_DB = "db/matrix-db";
	 private static GraphDatabaseService graphDb;
	 
	 /**
	  * creates a new instance of the matrix database and fills it
	  * @return the created database instance
	  */
	 public static GraphDatabaseService getMatrixDatabase(){
	    deleteFileOrDirectory( new File( MATRIX_DB ) );
	    graphDb = new EmbeddedGraphDatabase( MATRIX_DB );
	    createNodespace();
	    return graphDb;
	 }

	 
	 /**
	  * fills the database with nodes and relationships, using the matrix example
	  */
	 private static void createNodespace() {
	      Transaction tx = graphDb.beginTx();
	      try {
	    	 // create neo node
	         Node neo = graphDb.createNode();
	         neo.setProperty( "age", 29 );
	         neo.setProperty( "name", "Thomas Anderson" );	         
	         // connect neo to the reference node
	         Node referenceNode = graphDb.getReferenceNode();
	         referenceNode.createRelationshipTo( neo, RelTypes.NEO_NODE );
	         // create trinity node
	         Node trinity = graphDb.createNode();
	         trinity.setProperty( "name", "Trinity" );
	         Relationship rel = neo.createRelationshipTo( trinity, RelTypes.KNOWS );
	         rel.setProperty( "age", "3 days" );
	         // create morpheus node
	         Node morpheus = graphDb.createNode();
	         morpheus.setProperty( "name", "Morpheus" );
	         morpheus.setProperty( "occupation", "Total badass" );
	         morpheus.setProperty( "rank", "Captain" );	      
	         neo.createRelationshipTo( morpheus, RelTypes.KNOWS );
	         rel = morpheus.createRelationshipTo( trinity, RelTypes.KNOWS );
	         rel.setProperty( "age", "12 years" );
	         // create cypher node
	         Node cypher = graphDb.createNode();
	         cypher.setProperty( "last name", "Reagan" );
	         cypher.setProperty( "name", "Cypher" );	     
	         trinity.createRelationshipTo( cypher, RelTypes.KNOWS );
	         rel = morpheus.createRelationshipTo( cypher, RelTypes.KNOWS );
	         rel.setProperty( "disclosure", "public" );
	         // create smith node
	         Node smith = graphDb.createNode();
	         smith.setProperty( "language", "C++" );
	         smith.setProperty( "name", "Agent Smith" );
	         smith.setProperty( "version", "1.0b" );	        
	         rel = cypher.createRelationshipTo( smith, RelTypes.KNOWS );
	         rel.setProperty( "age", "6 months" );
	         rel.setProperty( "disclosure", "secret" );	        
	         // create architect node
	         Node architect = graphDb.createNode();
	         architect.setProperty( "name", "The Architect" );
	         smith.createRelationshipTo( architect, RelTypes.CODED_BY );
	 	     
	         tx.success();
	     }  finally {
	         tx.finish();
	     }
	 }

		 
	 
   private static void deleteFileOrDirectory( final File file ){
          if ( !file.exists() ){
               return;
          }
          
          if ( file.isDirectory() ){
            for ( File child : file.listFiles() ){
                deleteFileOrDirectory( child );
            }
          } else {
           file.delete();
          }
   }
 

}