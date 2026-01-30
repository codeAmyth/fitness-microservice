import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import { getActivityRecommendation } from "../services/api";

const ActivityDetail = () => {
  const { id } = useParams();
  const [rec, setRec] = useState(null);

  useEffect(() => {
    getActivityRecommendation(id)
      .then(res => setRec(res.data))
      .catch(err => console.error(err));
  }, [id]);

  if (!rec) return <p>Loading...</p>;

  return (
    <div style={{ padding: "20px" }}>
      <h2>🏃 Activity Recommendation</h2>

      <p><b>Type:</b> {rec.type}</p>

      <h3>Overall Analysis</h3>
      <p>{rec.recommendation}</p>

      <h3>Improvements</h3>
      <ul>
        {rec.improvements.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </ul>

      <h3>Suggestions</h3>
      <ul>
        {rec.suggestions.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </ul>

      <h3>Safety Tips</h3>
      <ul>
        {rec.safety.map((item, i) => (
          <li key={i}>{item}</li>
        ))}
      </ul>
    </div>
  );
};

export default ActivityDetail;
