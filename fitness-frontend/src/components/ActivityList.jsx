import React, { useEffect, useState } from "react";
import { getActivities } from "../services/api";
import {
  Grid,
  Card,
  CardContent,
  Typography
} from "@mui/material";
import { useNavigate } from "react-router-dom";

const ActivityList = () => {
  const [activities, setActivities] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    const fetchActivities = async () => {
      try {
        const res = await getActivities();
        setActivities(res.data);
      } catch (err) {
        console.error("Failed to fetch activities", err);
      }
    };

    fetchActivities();
  }, []);

  return (
    <Grid container spacing={2}>
      {activities.map((activity) => (
        <Grid item xs={12} md={6} key={activity.id}>
          <Card
            sx={{ cursor: "pointer" }}
            onClick={() => navigate(`/activities/${activity.id}`)}
          >
            <CardContent>
              <Typography variant="h6">{activity.type}</Typography>
              <Typography>
                Duration: {activity.duration} minutes
              </Typography>
              <Typography>
                Calories: {activity.caloriesBurned}
              </Typography>
            </CardContent>
          </Card>
        </Grid>
      ))}
    </Grid>
  );
};

export default ActivityList;
