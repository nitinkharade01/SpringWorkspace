import { NavLink, Outlet } from 'react-router-dom';
import { useAuth } from '../context/AuthContext.jsx';

export default function Shell() {
  const { user, logout, hasRole } = useAuth();
  const item = (to, icon, label, roles) => (!roles || hasRole(roles)) && <NavLink to={to}><i className={`bi ${icon}`} />{label}</NavLink>;
  return (
    <div className="app-shell">
      <aside>
        <div className="brand">Payment Reconciliation</div>
        <nav>
          {item('/', 'bi-speedometer2', 'Dashboard')}
          {item('/transactions', 'bi-credit-card', 'Transactions', ['ADMIN', 'FINANCE_USER'])}
          {item('/transactions/create', 'bi-plus-circle', 'Create Payment', ['ADMIN', 'FINANCE_USER'])}
          {item('/fraud-alerts', 'bi-shield-exclamation', 'Fraud Alerts', ['ADMIN', 'AUDITOR'])}
          {item('/reconciliation', 'bi-file-earmark-spreadsheet', 'Reconciliation', ['ADMIN', 'FINANCE_USER'])}
          {item('/reports', 'bi-bar-chart', 'Reports', ['ADMIN', 'AUDITOR'])}
          {item('/users', 'bi-people', 'Users', ['ADMIN'])}
          {item('/audit-logs', 'bi-clock-history', 'Audit Logs')}
        </nav>
      </aside>
      <main>
        <header>
          <div><strong>{user?.fullName}</strong><span>{user?.roles?.join(', ')}</span></div>
          <button onClick={logout}><i className="bi bi-box-arrow-right" />Logout</button>
        </header>
        <Outlet />
      </main>
    </div>
  );
}
