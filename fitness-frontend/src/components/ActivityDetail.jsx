import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  getActivityById,
  getActivityRecommendation
} from "../services/api";
import {
  Box,
  Card,
  CardContent,
  Divider,
  Typography
} from "@mui/material";

const ActivityDetail = () => {
  const { id } = useParams();

  const [activity, setActivity] = useState(null);
  const [recommendation, setRecommendation] = useState(null);
  const [loadingRec, setLoadingRec] = useState(true);

  /* ---- Fetch Activity ---- */
  useEffect(() => {
    const fetchActivity = async () => {
      try {
        const res = await getActivityById(id);
        setActivity(res.data);
      } catch (err) {
        console.error("Failed to fetch activity", err);
      }
    };

    fetchActivity();
  }, [id]);

  /* ---- Fetch Recommendation (Polling) ---- */
  useEffect(() => {
    const fetchRecommendation = async () => {
      try {
        const res = await getActivityRecommendation(id);
        setRecommendation(res.data);
        setLoadingRec(false);
      } catch (err) {
        if (err.response?.status === 404) {
          setTimeout(fetchRecommendation, 2000);
        } else {
          console.error("Failed to fetch recommendation", err);
          setLoadingRec(false);
        }
      }
    };

    fetchRecommendation();
  }, [id]);

  if (!activity) {
    return <Typography>Loading activity...</Typography>;
  }

  return (
    <Box sx={{ maxWidth: 800, mx: "auto", p: 2 }}>
      {/* -------- Activity Card -------- */}
      <Card sx={{ mb: 2 }}>
        <CardContent>
          <Typography variant="h5">Activity Details</Typography>
          <Typography>Type: {activity.type}</Typography>
          <Typography>
            Duration: {activity.duration} minutes
          </Typography>
          <Typography>
            Calories Burned: {activity.caloriesBurned}
          </Typography>
          <Typography>
            Date: {new Date(activity.createdAt).toLocaleString()}
          </Typography>
        </CardContent>
      </Card>

      {/* -------- Recommendation Card -------- */}
      <Card>
        <CardContent>
          <Typography variant="h5">AI Recommendation</Typography>

          {loadingRec && (
            <Typography>Generating recommendation...</Typography>
          )}

          {recommendation && (
            <>
              <Typography variant="h6">Analysis</Typography>
              <Typography paragraph>
                {recommendation.recommendation}
              </Typography>

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Improvements</Typography>
              {recommendation.improvements?.map((item, i) => (
                <Typography key={i}>• {item}</Typography>
              ))}

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Suggestions</Typography>
              {recommendation.suggestions?.map((item, i) => (
                <Typography key={i}>• {item}</Typography>
              ))}

              <Divider sx={{ my: 2 }} />

              <Typography variant="h6">Safety</Typography>
              {recommendation.safety?.map((item, i) => (
                <Typography key={i}>• {item}</Typography>
              ))}
            </>
          )}
        </CardContent>
      </Card>
    </Box>
  );
};

export default ActivityDetail;
