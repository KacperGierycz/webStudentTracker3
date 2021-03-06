package com.luv2code.web.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

public class StudentDbUtil {

	private DataSource dataSource;
		
	
	public StudentDbUtil(DataSource theDataSource) {
		dataSource=theDataSource;
}
	
	public List<Student> getStudents() throws Exception{
		
		List<Student> students=new ArrayList<>();
		
		Connection myConn=null;
		Statement myStmt=null;
		ResultSet myRs=null;
		
		try {
				
			// get a connection
			myConn=dataSource.getConnection();
				
			// create sql statement
			String sql="select * from student order by last_name";
			
			myStmt=myConn.createStatement();
				
			// execute query
			myRs=myStmt.executeQuery(sql);	
			
			// process result set
			while(myRs.next()) {
				
				// retrieve data from result set row
				int id=myRs.getInt("id");
				String firstName=myRs.getString("first_name");
				String lastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				// create new student object
				 Student tempStudent=
						 new Student(id,firstName,lastName,email);
				
				// add it to the list of students
				students.add(tempStudent);
				 
			}			
			
			return students;			
		}
		finally {
			// close JDBC object
			close(myConn, myStmt, myRs);
		}		
	}

	private void close(Connection myConn, Statement myStmt, ResultSet myRs) {

		try {
			if(myConn!=null) {
				myConn.close();
			}
			
			if(myStmt!=null) {
				myStmt.close();
			}
			
			if(myRs!=null) {
				myRs.close();	// doesn't really close it ... just puts back in connection pool
			}			
			
		}
		catch (Exception exc) {
			exc.printStackTrace();
			
		}		
	}

	public void addStudent(Student theStudent) throws Exception {
		
		
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
		// get db connection
		myConn=dataSource.getConnection();	
		
		// create sql for insert 
		String sql = "insert into student"
				+ "(first_name, last_name, email)"
				+ "values (?, ?, ?)";
		
		myStmt=myConn.prepareStatement(sql);
		
		// set the param values for the student
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		
		// execute sql insert
		myStmt.execute();
		
		}
		finally {
			// clean up JDBC objects			
			close(myConn, myStmt, null);
				
		}
			
	}

	 Student getStudent(String theStudentId) throws Exception {
	
		Student theStudent=null;
		
		Connection myConn=null;
		PreparedStatement myStm=null;
		ResultSet myRs=null;
		int studentId;
		
		try {
			// convert studentId to int
			studentId=Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to get selected student
			String sql ="select * from student where id=?";
			
			// create prepared statement
			myStm=myConn.prepareStatement(sql);
			
			// set params
			myStm.setInt(1, studentId);
			
			// execute statement
			myRs=myStm.executeQuery();
			
			// retrieve data from result set row
			if(myRs.next()) {
				String firstName=myRs.getString("first_name");
				String LastName=myRs.getString("last_name");
				String email=myRs.getString("email");
				
				// use the student during construction
				theStudent=new Student(studentId, firstName,LastName, email);
				
			}
			else {
				throw new Exception("Could not find student id: "+ studentId);
			}
					
		return theStudent;
		}
		finally{
			close(myConn, myStm, myRs);
			
		}

	}

	public void updateStudent(Student theStudent) throws Exception {
		
	
		Connection myConn=null;
		PreparedStatement myStmt=null;
		
		try {
		// get db connection
		myConn =dataSource.getConnection();
		
		// create SQL update statement
		String sql= "update student "
				+"set first_name=?, last_name=?, email=? "
				+"where id=?";
		
		// prepare statement
		myStmt=myConn.prepareStatement(sql);
		
		// set params
		myStmt.setString(1, theStudent.getFirstName());
		myStmt.setString(2, theStudent.getLastName());
		myStmt.setString(3, theStudent.getEmail());
		myStmt.setInt(4, theStudent.getId());
		
		
		// execute SQL statement
		myStmt.execute();
	}
	finally {
		close(myConn, myStmt, null);
	}
}

	public void deleteStudent(String theStudentId) throws Exception {
		
		Connection myConn = null;
		PreparedStatement myStmt=null;
		
		try {
			
			// convert student id to int
			int studentID=Integer.parseInt(theStudentId);
			
			// get connection to database
			myConn = dataSource.getConnection();
			
			// create sql to delete the student
			String sql ="delete from student where id=?";
			
			// prepared statement
			myStmt=myConn.prepareStatement(sql);
			
			// set parameters
			myStmt.setInt(1, studentID);
			
			// execute statement
			myStmt.execute();
			
		}
		finally {
			
			// close up JDBC code
			close(myConn, myStmt, null);
			
		}
	}
	
}






















