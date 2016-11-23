package ca.uoit.csci4100.samples.housing;

import java.util.List;

public interface HousingDownloadListener {
    void housingDataDownloaded(List<HousingProject> housingProjects);
}
