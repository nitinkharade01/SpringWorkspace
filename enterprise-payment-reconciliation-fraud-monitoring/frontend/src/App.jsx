import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom';
import ProtectedRoute from './routes/ProtectedRoute.jsx';
import Shell from './components/Shell.jsx';
import LoginPage from './pages/LoginPage.jsx';
import RegisterPage from './pages/RegisterPage.jsx';
import DashboardPage from './pages/DashboardPage.jsx';
import TransactionsPage from './pages/TransactionsPage.jsx';
import CreateTransactionPage from './pages/CreateTransactionPage.jsx';
import TransactionDetailsPage from './pages/TransactionDetailsPage.jsx';
import FraudAlertsPage from './pages/FraudAlertsPage.jsx';
import ReconciliationUploadPage from './pages/ReconciliationUploadPage.jsx';
import ReportsPage from './pages/ReportsPage.jsx';
import UsersPage from './pages/UsersPage.jsx';
import AuditLogsPage from './pages/AuditLogsPage.jsx';

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route path="/register" element={<RegisterPage />} />
        <Route path="/" element={<ProtectedRoute><Shell /></ProtectedRoute>}>
          <Route index element={<DashboardPage />} />
          <Route path="transactions" element={<ProtectedRoute roles={['ADMIN', 'FINANCE_USER']}><TransactionsPage /></ProtectedRoute>} />
          <Route path="transactions/create" element={<ProtectedRoute roles={['ADMIN', 'FINANCE_USER']}><CreateTransactionPage /></ProtectedRoute>} />
          <Route path="transactions/:id" element={<TransactionDetailsPage />} />
          <Route path="fraud-alerts" element={<ProtectedRoute roles={['ADMIN', 'AUDITOR']}><FraudAlertsPage /></ProtectedRoute>} />
          <Route path="reconciliation" element={<ProtectedRoute roles={['ADMIN', 'FINANCE_USER']}><ReconciliationUploadPage /></ProtectedRoute>} />
          <Route path="reports" element={<ProtectedRoute roles={['ADMIN', 'AUDITOR']}><ReportsPage /></ProtectedRoute>} />
          <Route path="users" element={<ProtectedRoute roles={['ADMIN']}><UsersPage /></ProtectedRoute>} />
          <Route path="audit-logs" element={<AuditLogsPage />} />
        </Route>
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </BrowserRouter>
  );
}
