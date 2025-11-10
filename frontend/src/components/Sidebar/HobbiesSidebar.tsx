import { useState, useMemo } from 'react';
import './HobbiesSidebar.css';
import { useAppSelector } from '../../redux/hooks';

interface HobbiesSidebarProps {
  onHobbyDragStart: (hobby: string) => void;
}

const HobbiesSidebar = ({ onHobbyDragStart }: HobbiesSidebarProps) => {
  const { users } = useAppSelector((state) => state.users);
  const [searchQuery, setSearchQuery] = useState('');

  // Extract all unique hobbies from users
  const allHobbies = useMemo(() => {
    const hobbyMap = new Map<string, number>();
    
    users.forEach((user) => {
      user.hobbies.forEach((hobby) => {
        hobbyMap.set(hobby, (hobbyMap.get(hobby) || 0) + 1);
      });
    });

    return Array.from(hobbyMap.entries())
      .map(([name, count]) => ({ name, count }))
      .sort((a, b) => b.count - a.count); // Sort by popularity
  }, [users]);

  // Filter hobbies based on search query
  const filteredHobbies = useMemo(() => {
    if (!searchQuery.trim()) return allHobbies;
    
    const query = searchQuery.toLowerCase();
    return allHobbies.filter((hobby) =>
      hobby.name.toLowerCase().includes(query)
    );
  }, [allHobbies, searchQuery]);

  const handleDragStart = (e: React.DragEvent, hobby: string) => {
    e.dataTransfer.setData('hobby', hobby);
    e.dataTransfer.effectAllowed = 'copy';
    onHobbyDragStart(hobby);
  };

  return (
    <div className="hobbies-sidebar">
      <h3 className="hobbies-sidebar-title">Hobbies</h3>

      <input
        type="text"
        placeholder="Search hobbies..."
        value={searchQuery}
        onChange={(e) => setSearchQuery(e.target.value)}
        className="hobbies-sidebar-search"
      />

      <div className="hobbies-sidebar-list">
        {filteredHobbies.length > 0 ? (
          filteredHobbies.map((hobby) => (
            <div
              key={hobby.name}
              className="hobby-item"
              draggable
              onDragStart={(e) => handleDragStart(e, hobby.name)}
            >
              <span className="hobby-item-name">{hobby.name}</span>
              <span className="hobby-item-count">{hobby.count}</span>
            </div>
          ))
        ) : (
          <div className="hobbies-sidebar-empty">
            {searchQuery ? 'No hobbies found' : 'No hobbies yet. Create users with hobbies!'}
          </div>
        )}
      </div>

      <div className="hobbies-sidebar-instruction">
        💡 <strong>Tip:</strong> Drag a hobby onto a user node in the graph to add it to that user.
      </div>
    </div>
  );
};

export default HobbiesSidebar;

