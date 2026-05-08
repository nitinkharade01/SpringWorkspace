import { useState } from 'react';
import api from '../api/client.js';

export default function ReconciliationUploadPage() {
  const [file, setFile] = useState(null);
  const [summary, setSummary] = useState(null);
  const submit = async e => { e.preventDefault(); const body = new FormData(); body.append('file', file); const res = await api.post('/api/reconciliation/upload', body); setSummary(res.data.data); };
  const download = async () => {
    const response = await api.get(`/api/reconciliation/download/${summary.fileId}`, { responseType: 'blob' });
    const url = URL.createObjectURL(response.data);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `reconciliation-${summary.fileId}.csv`;
    anchor.click();
    URL.revokeObjectURL(url);
  };
  return <div className="page"><h2>Reconciliation Upload</h2><form onSubmit={submit} className="upload"><input type="file" accept=".csv,.xlsx" onChange={e => setFile(e.target.files[0])} /><button disabled={!file}>Upload</button></form>{summary && <section><h3>Reconciliation Result</h3><div className="risk-row"><span>Total: {summary.totalRecords}</span><span>Matched: {summary.matchedCount}</span><span>Mismatched: {summary.mismatchedCount}</span><span>Duplicate: {summary.duplicateCount}</span><button onClick={download}>Download report</button></div></section>}</div>;
}
