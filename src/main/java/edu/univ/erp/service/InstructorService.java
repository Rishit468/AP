package edu.univ.erp.service;

import edu.univ.erp.data.DbPool;
import edu.univ.erp.data.ErpDao;
import edu.univ.erp.domain.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class InstructorService {

    private final ErpDao erpDao = new ErpDao();

    public Instructor getInstructorProfile(int userId) {
        return erpDao.getInstructorByUserId(userId);
    }

    public List<Section> getSectionsByInstructor(int instructorId) {
        return erpDao.getSectionsByInstructor(instructorId);
    }

    public List<Enrollment> getEnrollmentsBySection(int sectionId) {
        return erpDao.getEnrollmentsBySection(sectionId);
    }

    // ======================================================\
    // ⬇️ NEW METHODS FOR GRADE ENTRY ⬇️
    // ======================================================\

    /**
     * Gets all grades for a given section, formatted for the UI table.
     * @param sectionId The section to get grades for.
     * @return A map where Key = enrollmentId, Value = (Map of Component -> Score)
     */
    public Map<Integer, Map<String, Double>> getGradesForSection(int sectionId) {
        return erpDao.getGradesBySection(sectionId);
    }

    /**
     * Saves a list of grades.
     * Checks for maintenance mode before saving.
     * @param gradesToSave A list of Grade objects to save.
     * @return true if successful, false if in maintenance mode or on error.
     */
    public boolean saveGrades(List<Grade> gradesToSave) {
        // 1. Check Maintenance Mode.
        // We can pass null for the user, as canMakeChanges() only checks
        // the global static flag when the user is not an admin.
        if (!AccessChecker.canMakeChanges(null)) {
            System.err.println("Grade save blocked: Maintenance Mode is ON.");
            return false;
        }

        // 2. Save all grades in a transaction
        try (var conn = DbPool.getErpDataSource().getConnection()) {
            conn.setAutoCommit(false); // Start transaction

            try {
                for (Grade grade : gradesToSave) {
                    // Use the "upsert" method for each grade
                    erpDao.upsertGrade(
                            grade.getEnrollmentId(),
                            grade.getComponent(),
                            grade.getScore()
                    );
                }
                conn.commit(); // Commit all changes at once
                return true;
            } catch (SQLException e) {
                conn.rollback(); // Rollback on any error
                e.printStackTrace();
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
