package testutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;

public class AutoGrader {

	// Test if the code correctly implements the Thread class and Thread creation
	public boolean testThreadCreation(String filePath) throws IOException {
		System.out.println("Starting testThreadCreation with file: " + filePath);

		File participantFile = new File(filePath); // Path to participant's file
		if (!participantFile.exists()) {
			System.out.println("File does not exist at path: " + filePath);
			return false;
		}

		FileInputStream fileInputStream = new FileInputStream(participantFile);
		JavaParser javaParser = new JavaParser();
		CompilationUnit cu;
		try {
			cu = javaParser.parse(fileInputStream).getResult()
					.orElseThrow(() -> new IOException("Failed to parse the Java file"));
		} catch (IOException e) {
			System.out.println("Error parsing the file: " + e.getMessage());
			throw e;
		}

		System.out.println("Parsed the Java file successfully.");

		// Flags to check for thread class usage
		boolean hasMainMethod = false;
		boolean extendsThreadClass = false;
		boolean startsThread1 = false;
		boolean startsThread2 = false;

		// Check for method declarations
		for (MethodDeclaration method : cu.findAll(MethodDeclaration.class)) {
			String methodName = method.getNameAsString();
			// Check for the presence of the main method
			if (methodName.equals("main")) {
				hasMainMethod = true;
			}
		}

		// Check for class declarations
		for (ClassOrInterfaceDeclaration classDecl : cu.findAll(ClassOrInterfaceDeclaration.class)) {
			if (classDecl.getNameAsString().equals("MyThread")) {
				extendsThreadClass = true;
			}
		}

		// Check for thread start method calls
		for (ExpressionStmt stmt : cu.findAll(ExpressionStmt.class)) {
			if (stmt.getExpression() instanceof MethodCallExpr) {
				MethodCallExpr methodCall = (MethodCallExpr) stmt.getExpression();
				if (methodCall.getNameAsString().equals("start")) {
					if (methodCall.getScope().get().toString().contains("thread1")) {
						startsThread1 = true;
					}
					if (methodCall.getScope().get().toString().contains("thread2")) {
						startsThread2 = true;
					}
				}
			}
		}

		// Log results of the checks
		System.out.println("Method 'main' is " + (hasMainMethod ? "present" : "NOT present"));
		System.out.println("Class 'MyThread' extends Thread: " + (extendsThreadClass ? "YES" : "NO"));
		System.out.println("Thread 1 is started: " + (startsThread1 ? "YES" : "NO"));
		System.out.println("Thread 2 is started: " + (startsThread2 ? "YES" : "NO"));

		// Final result - all conditions should be true
		boolean result = hasMainMethod && extendsThreadClass && startsThread1 && startsThread2;

		System.out.println("Test result: " + result);
		return result;
	}
}
