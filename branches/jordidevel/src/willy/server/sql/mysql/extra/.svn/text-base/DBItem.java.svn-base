package willy.server.sql.mysql.extra;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DBItem {

        protected String table_schema=null;
        protected String table_name=null;
        protected String table_type=null;
        public String getTableType() { return this.table_type; }
        
        protected String engine=null;
        protected Long data_length=null;
        public Long getDataLength() { return this.data_length; }
        public String getDataLengthString() { if(this.data_length==null) return "NULL"; else return this.data_length.toString(); }
        
        protected Long data_free=null;
        public Long getDataFree() { return this.data_free; }
        public String getDataFreeString() { if(this.data_free==null) return "NULL"; else return this.data_free.toString(); }
        
        protected String table_comment=null;

        public DBItem(ResultSet rs) throws SQLException
        {
                //"select table_schema,table_name,table_type,engine,data_length,data_free,table_comment"
                //              1           2         3         4         5         6           7
                //
                this.table_schema  = rs.getString(1);
                this.table_name    = rs.getString(2);
                this.table_type    = rs.getString(3);
                this.engine        = rs.getString(4);
                try{ this.data_length   = Long.parseLong(rs.getString(5)); } 
                	catch(Exception e) {this.data_length=null;} 
                try{ this.data_free     = Long.parseLong(rs.getString(6)); }
                	catch(Exception e) {this.data_length=null;} 
                this.table_comment = rs.getString(7);

                if(this.engine!=null) this.engine=this.engine.toLowerCase();
        }

        public String toString()
        {
                return "@table_schema=" +this.table_schema+
                                ",table_name=" +this.table_name+
                                ",table_type=" +this.table_type+
                                ",engine=" +this.engine+
                                ",data_length=" +this.data_length+
                                ",data_free=" + this.data_free+
                                ",table_comment="+this.table_comment+"@";
        }

        /**
         * Comparar sense classe
         * @param engine
         * @param type
         * @return
         */
        public static boolean isFailed(String engine, String type)
        {
        	return (
                    (engine==null)
                    &&
                    (type.compareTo("VIEW")!=0)
                    );
        }
        
        public boolean isFailed()
        {
                return (
                                (this.engine==null)
                                &&
                                (!this.isView())
                                );
        }

        public boolean isView()
        {
                return this.table_type.compareTo("VIEW")==0;
        }

        public String getTableComment()
        {
                return this.table_comment;
        }

        public String getSchema()
        {
                return this.table_schema;
        }

        public String getTableName()
        {
                return this.table_name;
        }

        public String getSchemaAndTableName()
        {
                return this.table_schema+"."+this.table_name;
        }

        public String getEngine()
        {
                return this.engine;
        }
}
